package dev.uncandango.alltheleaks.mixin.core.main;

import it.hurts.octostudios.octolib.module.particle.OctoRenderManager;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OctoRenderManager.class)
public class OctoRenderManagerMixin {
	@Inject(method = "registerProvider", at = @At("HEAD"), cancellable = true)
	private static void cancelIfServerSide(@Coerce Object provider, CallbackInfo ci) {
		if (provider instanceof Entity entity && !entity.level().isClientSide()) {
			ci.cancel();
		}
	}
}
