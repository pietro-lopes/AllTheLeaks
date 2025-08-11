package dev.uncandango.alltheleaks.metrics.client.mods.jei;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Issue(modId = "jei", issueId = "ItemStackCreationStatistics", versionRange = "[15.4.0.9,)", devOnly = true, mixins = {"main.ATLItemStackMixin$Statistics", "main.PluginCallerMixin"}, description = "Adds metrics to see which JEI Plugins are creating more ItemStacks")
public class ItemStackCreationStatistics {
	public static final Map<ResourceLocation, Map<String, Long>> ITEMSTACK_COUNTER = new ConcurrentHashMap<>();
	public static Pair<ResourceLocation, String> currentPlugin = null;

	public static void addToCounter() {
		ITEMSTACK_COUNTER.computeIfAbsent(currentPlugin.getFirst(), k -> Maps.newConcurrentMap()).compute(currentPlugin.getSecond(), (task, count) -> count == null ? 1L : count + 1L);
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
