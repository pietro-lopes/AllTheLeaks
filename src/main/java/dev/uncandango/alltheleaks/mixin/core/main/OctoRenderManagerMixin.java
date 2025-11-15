package dev.uncandango.alltheleaks.mixin.core.main;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = {"it.hurts.octostudios.octolib.module.particle.OctoRenderManager","it.hurts.octostudios.octolib.modules.particles.OctoRenderManager"})
public class OctoRenderManagerMixin {
	@Inject(method = "registerProvider", at = @At("HEAD"), cancellable = true)
	private static void cancelIfServerSide(@Coerce Object provider, CallbackInfo ci) {
		if (provider instanceof Entity entity && !entity.level().isClientSide()) {
			ci.cancel();
		}
	}
}
