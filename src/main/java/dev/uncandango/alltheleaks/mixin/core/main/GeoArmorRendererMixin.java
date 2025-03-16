package dev.uncandango.alltheleaks.mixin.core.main;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

@Mixin(value = GeoArmorRenderer.class, remap = false)
public abstract class GeoArmorRendererMixin {
	@Shadow
	protected Entity currentEntity;

	@Inject(method = "doPostRenderCleanup", at = @At("RETURN"))
	private void doPostRenderCleanup(CallbackInfo ci) {
		currentEntity = null;
	}
}
