package dev.uncandango.alltheleaks.metrics.client.mods.jei;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

@Issue(modId = "jei", issueId = "ItemStackCreationStatistics", versionRange = "[15.4.0.9,)", devOnly = true, mixins = {"main.ATLItemStackMixin$Statistics", "main.PluginCallerMixin"}, description = "Adds metrics to see which JEI Plugins are creating more ItemStacks")
public class ItemStackCreationStatistics {
	public static final Map<ResourceLocation, Map<String, Long>> ITEMSTACK_COUNTER = Maps.newHashMap();
	public static Pair<ResourceLocation, String> currentPlugin = null;

	public static void addToCounter() {
		ITEMSTACK_COUNTER.compute(currentPlugin.getFirst(), (k, v) -> {
			if (v == null) {
				v = new HashMap<>();
			}
			v.merge(currentPlugin.getSecond(), 1L, Long::sum);
			return v;
		});
	}

	@SuppressWarnings("CodeBlock2Expr")
	public static void printSummary() {
		ITEMSTACK_COUNTER.forEach((plugin, taskMap) -> {
			AllTheLeaks.LOGGER.info("|-> Plugin: {}", plugin);
			taskMap.forEach((task, count) -> {
				AllTheLeaks.LOGGER.info("|--> Task: {} -> Stacks instanced: {}", task, count);
			});
		});
	}
}
