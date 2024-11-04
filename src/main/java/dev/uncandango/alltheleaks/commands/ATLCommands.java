package dev.uncandango.alltheleaks.commands;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.gson.JsonElement;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.datafixers.util.Pair;
import com.sun.management.HotSpotDiagnosticMXBean;
import dev.uncandango.alltheleaks.AllTheLeaks;
import mezz.jei.core.collect.SetMultiMap;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.HashSet;

public final class ATLCommands {

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("atl")
                        .then(
							Commands.literal("run_full_gc")
								.executes(cmd -> runGc(cmd.getSource()))
						)
						.then(Commands.literal("dump_ingredient_duplicates")
							.executes(cmd -> dumpIngredientDuplicates(cmd.getSource()))
						)
		);
    }

    @SuppressWarnings("SameReturnValue")
	public static int runGc(CommandSourceStack source) {
		try {
			var server = ManagementFactory.getPlatformMBeanServer();
			var hotSpotDiagnosticMXBean = ManagementFactory.newPlatformMXBeanProxy(
				server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
			var gcDisabled = Boolean.parseBoolean(hotSpotDiagnosticMXBean.getVMOption("DisableExplicitGC").getValue());
			if (gcDisabled) {
				source.sendFailure(Component.literal("Explicit GC is disabled, remove arguments -XX:+DisableExplicitGC"));
				return 0;
			}
		} catch (Exception e) {
			AllTheLeaks.LOGGER.error("Error while instancing MXBean: {}", e.getMessage());
			return 0;
		}
		System.gc();
		return 1;
    }

	public static int dumpIngredientDuplicates(CommandSourceStack source) {
		Multimap<JsonElement, Pair<Ingredient, ResourceLocation>> recipeJsonToIngredientMap = HashMultimap.create();
		Minecraft.getInstance().level.getRecipeManager().getRecipes().forEach(recipe -> {
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
				});
			}
		});
		return 1;
	}
}
