package dev.uncandango.alltheleaks.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.datafixers.util.Pair;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.config.ATLProperties;
import dev.uncandango.alltheleaks.diag.common.mods.minecraft.DebugChunkLoading;
import dev.uncandango.alltheleaks.diag.common.mods.minecraft.DebugNativeImage;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.MemoryMonitor;
import dev.uncandango.alltheleaks.mixin.Trackable;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.commons.lang3.mutable.MutableObject;
import org.embeddedt.modernfix.world.ThreadDumper;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public final class ATLCommands {

	public static void registerClientCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
		dispatcher.register(
			Commands.literal("atl")
				.then(
					Commands.literal("run_explicit_gc")
						.executes(cmd -> runGc(cmd.getSource()))
				)
				.then(Commands.literal("clear_native_images")
					.executes(cmd -> clearNativeImages(cmd.getSource()))
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
					Commands.literal("track_chunk").requires(source -> ATLProperties.get().debugChunkLoading)
						.executes(cmd -> clearTrackingChunks(cmd.getSource()))
						.then(
							Commands.argument("x", IntegerArgumentType.integer())
								.then(
									Commands.argument("z", IntegerArgumentType.integer())
										.executes(cmd -> startTrackingChunks(cmd.getSource(), IntegerArgumentType.getInteger(cmd, "x"), IntegerArgumentType.getInteger(cmd, "z"), null))
										.then(
											Commands.argument("dimension", DimensionArgument.dimension())
												.executes(cmd -> startTrackingChunks(cmd.getSource(), IntegerArgumentType.getInteger(cmd, "x"), IntegerArgumentType.getInteger(cmd, "z"), DimensionArgument.getDimension(cmd, "dimension")))
										)
								)
						)
				)
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

		var validBes = source.registryAccess().lookupOrThrow(Registries.BLOCK_ENTITY_TYPE).listElements()
			.map(Holder.Reference::value)
			.flatMap(bet -> bet.getValidBlocks().stream())
			.map(Block::builtInRegistryHolder)
			.map(Holder.Reference::getKey)
			.filter(Objects::nonNull)
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
			public void onLevelTick(LevelTickEvent.Post event) {
				if (!event.getLevel().equals(player.level())) {
					return;
				}
				while (event.hasTime() && !setBlocksQueue.isEmpty()) {
					var job = setBlocksQueue.poll();
					var pos = job.getFirst();
					var be = job.getSecond();
					AllTheLeaks.LOGGER.info("Placing {} at {} {}", be, dimension, pos.toShortString());
					String commandString = "execute in " + dimension + " run setblock " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " " + be;
					server.getCommands().performPrefixedCommand(player.createCommandSourceStack().withSuppressedOutput(), commandString);
				}
				if (setBlocksQueue.isEmpty()) {
					player.sendSystemMessage(Component.literal("Placing blocks finished!"));
					NeoForge.EVENT_BUS.unregister(eventListener.getValue());
				} else {
					if (setBlocksQueue.size() < initialValue * (1 - (threshold / 10.0))) {
						player.sendSystemMessage(Component.translatable("%s blocks remaining...", setBlocksQueue.size()));
						threshold++;
					}
				}
			}
		});

		NeoForge.EVENT_BUS.register(eventListener.getValue());

		return 0;
	}

	public static int startTrackingChunks(CommandSourceStack source, int x, int z, @Nullable ServerLevel level) {
		if (level == null) {
			level = source.getLevel(); // Defaults to overworld if using server console
		}
		var chunkPos = new ChunkPos(x, z);
		var dimension = level.dimension();
		DebugChunkLoading.addTrackingChunk(dimension, chunkPos);
		source.sendSuccess(() -> Component.translatable("Starting to track events for chunk %s from dimension %s", Component.literal(chunkPos.toString()).withStyle(ChatFormatting.GREEN), Component.literal(dimension.location().toString()).withStyle(ChatFormatting.YELLOW)), true);
		return 1;
	}

	public static int clearTrackingChunks(CommandSourceStack source) {
		DebugChunkLoading.clearTrackingChunks();
		source.sendSuccess(() -> Component.literal("Cleared tracking chunks, now tracking everything."), true);
		return 1;
	}

	public static int checkLeaking(CommandSourceStack source, boolean shouldRunGc) {
		if (shouldRunGc) {
			if (runGc(source) == 0) {
				return 0;
			}
		}
		Trackable.clearNullReferences();

		// Events for logging

		AllTheLeaks.LOGGER.info("Logging events from checking leak");
		MemoryMonitor.getEventsSummary().forEach(AllTheLeaks.LOGGER::info);

		List<Component> lines = new ArrayList<>();
		Trackable.getSummary().forEach((baseClazz, summaryMap) -> {
			if (summaryMap.isEmpty()) {
				return;
			}
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
		if (lines.isEmpty()) {
			source.sendSystemMessage(Component.literal("No leak was found so far...").withStyle(ChatFormatting.GREEN));
		} else {
			source.sendSystemMessage(Component.literal("Listing leaks...").withStyle(ChatFormatting.YELLOW));
			lines.forEach(source::sendSystemMessage);
		}
		return Command.SINGLE_SUCCESS;
	}

	private static int clearNativeImages(CommandSourceStack source) {
		if (!ATLProperties.get().debugNativeImage) {
			source.sendFailure(Component.literal("DebugNativeImage is disabled, activate it at config/alltheleaks.json"));
			return 0;
		}
		AtomicInteger counter = new AtomicInteger();
		DebugNativeImage.NATIVE_IMAGES_TRACKER.forEach((k, v) -> {
			var setWithEmptyRef = Collections.synchronizedSet(new HashSet<DebugNativeImage.Value>());
			v.forEach(wr -> {
				var ref = wr.imageRef().get();
				if (ref == null) {
					setWithEmptyRef.add(wr);
				}
			});
			if (!v.isEmpty() && setWithEmptyRef.size() == v.size()) {
				AllTheLeaks.LOGGER.info("Removed all NativeImages for key {}", k);
				if (k.useStbFree()) {
					STBImage.nstbi_image_free(k.pixels());
				} else {
					MemoryUtil.nmemFree(k.pixels());
				}
				setWithEmptyRef.forEach(wr -> {
					counter.getAndIncrement();
					AllTheLeaks.LOGGER.info("Printing stack trace for: {}", wr.description());
					var reachedInit = false;
					for (StackTraceElement trace : wr.stackTraceElements()) {
						if (!reachedInit) {
							reachedInit = trace.getMethodName().contains("init");
						}
						if (reachedInit) {
							System.out.println("\tat " + trace.getClassName() + "." + trace.getMethodName() +
								"(" + trace.getFileName() + ":" + trace.getLineNumber() + ")");
						}

					}
				});
				v.clear();
			}
		});
		source.sendSuccess(() -> Component.literal("Cleared " + counter.get() + " NativeImages"), true);
		return 1;
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
}