package dev.uncandango.alltheleaks.mixin.core.main;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

@Mixin(value = GeoArmorRenderer.class, remap = false)
public class GeoArmorRendererMixin {
	@Shadow
	protected Entity currentEntity;

	@Inject(method = "renderToBuffer", at = @At(value = "FIELD", target = "Lsoftware/bernie/geckolib/renderer/GeoArmorRenderer;animatable:Lnet/minecraft/world/item/Item;", shift = At.Shift.AFTER, ordinal = 3))
	private void atl$clearEntity(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int colour, CallbackInfo ci) {
		this.currentEntity = null;
	}
}
