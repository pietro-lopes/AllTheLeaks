package dev.uncandango.alltheleaks.commands;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.datafixers.util.Pair;
import dev.gcpath.api.GcPathSession;
import dev.gcpath.api.GcPaths;
import dev.gcpath.api.RenderOptions;
import dev.gcpath.api.TrackedObject;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.ClearLeakedLevelChunks;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.MemoryMonitor;
import dev.uncandango.alltheleaks.mixin.Trackable;
import dev.uncandango.alltheleaks.mixin.core.main.IngredientMixin;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.commons.lang3.mutable.MutableObject;
import org.embeddedt.modernfix.world.ThreadDumper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class ATLCommands {

    public static void registerClientCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("atl")
                        .then(
							Commands.literal("run_explicit_gc")
								.executes(cmd -> runGc(cmd.getSource()))
						)
						.then(Commands.literal("dump_ingredient_duplicates").requires(source -> AllTheLeaks.INDEV)
							.executes(cmd -> dumpIngredientDuplicates(cmd.getSource()))
						)
						.then(
							Commands.literal("force_refresh")
								.executes(cmd -> checkLeaking(cmd.getSource(), true))
						)
						.then(
							Commands.literal("reset_statistics")
								.executes(cmd -> resetStatistics(cmd.getSource()))
						)
						.then(
							Commands.literal("thread_dump").requires(source -> ModList.get().isLoaded("modernfix"))
								.executes(cmd -> doModernFixThreadDump(cmd.getSource()))
						)
						.then(
							Commands.literal("print_gc_roots")
								.executes(cmd -> printGcRootOfClientLeaks(cmd.getSource()))
						)
		);
    }

	private static int doModernFixThreadDump(CommandSourceStack source) {
		AllTheLeaks.LOGGER.error(ThreadDumper.obtainThreadDump());
		source.sendSystemMessage(Component.literal("Thread dump done, check latest.log"));
		return Command.SINGLE_SUCCESS;
	}

	private static int resetStatistics(CommandSourceStack source) {
		runGc(source);
		MemoryMonitor.Statistics.reset();
		return Command.SINGLE_SUCCESS;
	}

	public static void registerCommonCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
		dispatcher.register(
			Commands.literal("atl")
				.then(
					Commands.literal("place_all_block_entities").requires(source -> AllTheLeaks.INDEV && source.getServer().isSingleplayer())
						.executes(cmd -> placeAllBes(cmd.getSource()))
				)
		);
	}

	public static void registerServerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
		dispatcher.register(
			Commands.literal("atl")
				.then(
					Commands.literal("run_explicit_gc")
						.executes(cmd -> runGc(cmd.getSource()))
				)
				.then(
					Commands.literal("force_refresh")
						.executes(cmd -> checkLeaking(cmd.getSource(), true))
				)
				.then(
					Commands.literal("reset_statistics")
						.executes(cmd -> resetStatistics(cmd.getSource()))
				)
				.then(
					Commands.literal("thread_dump").requires(source -> ModList.get().isLoaded("modernfix"))
						.executes(cmd -> doModernFixThreadDump(cmd.getSource()))
				)
		);
	}

	public static int printGcRootOfClientLeaks(CommandSourceStack source){
		Set<TrackedObject<?>> leaked = new HashSet<>();
		Trackable.TRACKABLE_MAP.forEach((key, value) -> {
			value.forEach(weakRef -> {
				var leakedValue = weakRef.get();
				if (leakedValue != null) {
					leaked.add(GcPaths.track(leakedValue));
				}
			});
		});

		System.setProperty("gcpath.mat.runtime", AllTheLeaks.getLocal().resolve("mat").toAbsolutePath().toString());
		System.setProperty("gcpath.capture.directory", AllTheLeaks.getLocal().toAbsolutePath().toString());
		System.setProperty("gcpath.mat.progress", "true");

		var probe = GcPaths.probe(leaked.toArray(TrackedObject[]::new));

		probe.withMappingFile(AllTheLeaks.getLocal().resolve("minecraft_1_20_1.map"));

		Util.backgroundExecutor().submit(() -> {
			try (GcPathSession session = probe.capture()) {
				leaked.forEach(tracked -> {
					var report = session.shortestStrongPath(tracked);
					AllTheLeaks.LOGGER.info(report.render(RenderOptions.defaults()));
				});
			}
		});

		return 0;
	}

	public static int checkLeaking(CommandSourceStack source, boolean shouldRunGc) {
		ClearLeakedLevelChunks.execute();
		if (shouldRunGc) {
			if (runGc(source) == 0) return 0;
		}
		Trackable.clearNullReferences();

		// Events for logging

		AllTheLeaks.LOGGER.info("Logging events from checking leak");
		MemoryMonitor.getEventsSummary().forEach(AllTheLeaks.LOGGER::info);

		List<Component> lines = new ArrayList<>();
		Trackable.getSummary().forEach((baseClazz, summaryMap) -> {
			if (summaryMap.isEmpty()) return;
			lines.add(Component.translatable("%s:", baseClazz.getSimpleName()));
			summaryMap.forEach((innerClazz, count) -> {
				var module = innerClazz.getModule();
				if (module != null) {
					lines.add(Component.translatable("- %s (%s): %s", innerClazz.getSimpleName(), module.getName(), count));
				} else {
					lines.add(Component.translatable("- %s: %s", innerClazz.getSimpleName(), count));
				}
			});
		});
		if (lines.isEmpty()){
			source.sendSystemMessage(Component.literal("No leak was found so far...").withStyle(ChatFormatting.GREEN));
		} else {
			source.sendSystemMessage(Component.literal("Listing leaks...").withStyle(ChatFormatting.YELLOW));
			lines.forEach(source::sendSystemMessage);
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int runGc(CommandSourceStack source) {
		var server = ServerLifecycleHooks.getCurrentServer();
		if (server != null) {
			server.executeBlocking(() -> server.saveEverything(true, true, true));
		}
		if (!MemoryMonitor.runExplicitGc()) {
			source.sendFailure(Component.literal("Explicit GC is disabled, remove arguments -XX:+DisableExplicitGC"));
			return 0;
		}
		return Command.SINGLE_SUCCESS;
    }

	public static int dumpIngredientDuplicates(CommandSourceStack source) {
		Multimap<JsonElement, Pair<Ingredient, ResourceLocation>> recipeJsonToIngredientMap = HashMultimap.create();
		source.getRecipeManager().getRecipes().forEach(recipe -> {
			var ingredients = recipe.getIngredients();
			for (Ingredient ingredient : ingredients) {
				var jsonElement = ingredient.toJson();
				var recipeId = recipe.getId();
				var key = Pair.of(ingredient, recipeId);
				recipeJsonToIngredientMap.put(jsonElement, key);
			}
		});

		recipeJsonToIngredientMap.asMap().forEach((key, value) -> {
			var innerIngredientSet = new HashSet<Ingredient>();
			value.forEach(pair -> innerIngredientSet.add(pair.getFirst()));
			if (innerIngredientSet.size() > 1) {
				AllTheLeaks.LOGGER.warn("Ingredients with the same json: {}", key);
				innerIngredientSet.forEach(ingredient -> {
					AllTheLeaks.LOGGER.warn("  - {}", ingredient);
					if (ingredient.isVanilla()) {
						if (ingredient instanceof IngredientMixin.IngredientAccessor accessor) {
							for (var ivalue : accessor.getValues()) {
								if (ivalue instanceof IngredientMixin.ItemValueAccessor iv) {
									var is = iv.getItem();
									AllTheLeaks.LOGGER.warn("    - {} - {}", is.getItemHolder().unwrapKey().get().location(), is.getTag());
								}
								if (ivalue instanceof Ingredient.TagValue tv) {
									AllTheLeaks.LOGGER.warn("    - {}", tv.serialize());
								}
							}
						}
					}
				});
			}
		});
		return 1;
	}

	private static int placeAllBes(CommandSourceStack source) {
		Set<ResourceLocation> ignoredBes = new HashSet<>();
		Set<String> ignoredMods = new HashSet<>();

		Path ignoredFile = AllTheLeaks.getLocal().resolve("ignored_block_entities.txt");
		Path ignoredModsFile = AllTheLeaks.getLocal().resolve("ignored_mods.txt");

		if (Files.exists(ignoredFile)) {
			try {
				Files.readAllLines(ignoredFile).stream()
					.map(ResourceLocation::tryParse)
					.filter(Objects::nonNull)
					.forEach(ignoredBes::add);
			} catch (IOException e) {
				AllTheLeaks.LOGGER.error("Error while trying to read file", e);
			}
		}

		if (Files.exists(ignoredModsFile)) {
			try {
				ignoredMods.addAll(Files.readAllLines(ignoredModsFile));
			} catch (IOException e) {
				AllTheLeaks.LOGGER.error("Error while trying to read file", e);
			}
		}

		var validBeVarHandle = ReflectionHelper.getFieldFromClass(BlockEntityType.class, "f_58915_", Set.class, false);

		var validBes = source.registryAccess().lookupOrThrow(Registries.BLOCK_ENTITY_TYPE).listElements()
			.map(Holder.Reference::value)
			.flatMap(bet -> ((Set<Block>)validBeVarHandle.get(bet)).stream())
			.map(Block::builtInRegistryHolder)
			.map(Holder.Reference::unwrapKey)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map(ResourceKey::location)
			.filter(rl -> !ignoredBes.contains(rl) && !ignoredMods.contains(rl.getNamespace()))
			.toList();

		var player = Objects.requireNonNull(source.getPlayer());
		var dimension = player.level().dimension().location();
		var server = source.getServer();
		int beIndex = 0;
		int spiralRadius = (int) Math.ceil((Math.sqrt(validBes.size()) - 1) / 2);

		var setBlocksQueue = new ConcurrentLinkedQueue<Pair<BlockPos, String>>();

		for (var pos : BlockPos.spiralAround(player.blockPosition(), spiralRadius, Direction.EAST, Direction.SOUTH)) {
			if (beIndex >= validBes.size()) {
				break;
			}
			var be = validBes.get(beIndex).toString();
			setBlocksQueue.add(Pair.of(pos.immutable(), be));

			beIndex++;
		}

		player.sendSystemMessage(Component.translatable("Placing %s block entities...", setBlocksQueue.size()));

		MutableObject<Object> eventListener = new MutableObject<>();
		eventListener.setValue(new Object() {
			final int initialValue = setBlocksQueue.size();
			int threshold = 1;

			@SubscribeEvent
			public void onLevelTick(TickEvent.LevelTickEvent event) {
				if (event.phase != TickEvent.Phase.END || !event.level.equals(player.level())) {
					return;
				}
				while (event.haveTime() && !setBlocksQueue.isEmpty()) {
					var job = setBlocksQueue.poll();
					var pos = job.getFirst();
					var be = job.getSecond();
					AllTheLeaks.LOGGER.info("Placing {} at {} {}", be, dimension, pos.toShortString());
					String commandString = "execute in " + dimension + " run setblock " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " " + be;
					server.getCommands().performPrefixedCommand(player.createCommandSourceStack().withSuppressedOutput(), commandString);
				}
				if (setBlocksQueue.isEmpty()) {
					player.sendSystemMessage(Component.literal("Placing blocks finished!"));
					MinecraftForge.EVENT_BUS.unregister(eventListener.getValue());
				} else {
					if (setBlocksQueue.size() < initialValue * (1 - (threshold / 10.0))) {
						player.sendSystemMessage(Component.translatable("%s blocks remaining...", setBlocksQueue.size()));
						threshold++;
					}
				}
			}
		});

		MinecraftForge.EVENT_BUS.register(eventListener.getValue());

		return 0;
	}
}
