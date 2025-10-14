package dev.uncandango.alltheleaks.mixin.core.main;

import me.lucko.spark.forge.ForgeSparkMod;
import me.lucko.spark.forge.plugin.ForgeServerSparkPlugin;
import me.lucko.spark.forge.plugin.ForgeSparkPlugin;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ForgeServerSparkPlugin.class, remap = false)
public abstract class ForgeServerSparkPluginMixin extends ForgeSparkPlugin {

	@Shadow
	@Final
	@Mutable
	private MinecraftServer server;

	protected ForgeServerSparkPluginMixin(ForgeSparkMod mod) {
		super(mod);
	}

	@Inject(method = "disable", at = @At("TAIL"))
	private void clearServer(CallbackInfo ci){
		this.server = null;
		this.platform = null;
	}
}
