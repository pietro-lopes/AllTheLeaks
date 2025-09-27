package dev.uncandango.alltheleaks.commands;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.datafixers.util.Pair;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.ClearLeakedLevelChunks;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.MemoryMonitor;
import dev.uncandango.alltheleaks.mixin.Trackable;
import dev.uncandango.alltheleaks.mixin.core.main.IngredientMixin;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.embeddedt.modernfix.world.ThreadDumper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
}
