package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.metrics.client.mods.jei.ItemStackCreationStatistics;
import mezz.jei.api.IModPlugin;
import mezz.jei.library.load.PluginCaller;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;

@Mixin(value = PluginCaller.class, remap = false)
public class PluginCallerMixin {
	@Inject(method = "callOnPlugins", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
	private static void setActivePlugin(String title, List<IModPlugin> plugins, Consumer<IModPlugin> func, CallbackInfo ci, @Local ResourceLocation pluginUid){
		ItemStackCreationStatistics.currentPlugin = new Pair<>(pluginUid, title);
	}

	@Inject(method = "callOnPlugins", at = @At(value = "TAIL"))
	private static void reportStatistics(String title, List<IModPlugin> plugins, Consumer<IModPlugin> func, CallbackInfo ci){
		AllTheLeaks.LOGGER.info("Jei Task: {}", title);
		ItemStackCreationStatistics.printSummary();
		ItemStackCreationStatistics.currentPlugin = null;
	}
}
