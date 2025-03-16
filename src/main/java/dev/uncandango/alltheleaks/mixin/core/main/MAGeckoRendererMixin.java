package dev.uncandango.alltheleaks.mixin.core.main;

import com.mna.entities.renderers.MAGeckoRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Mixin(value = MAGeckoRenderer.class, remap = false)
public abstract class MAGeckoRendererMixin<T extends LivingEntity & GeoAnimatable> extends GeoEntityRenderer<T> {

	public MAGeckoRendererMixin(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
		super(renderManager, model);
	}

	@Inject(method = "defaultRender(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/VertexConsumer;FFI)V", at = @At("RETURN"))
	private void atl$addPostCleanup(PoseStack poseStack, T animatable, MultiBufferSource bufferSource, RenderType renderType, VertexConsumer buffer, float yaw, float partialTick, int packedLight, CallbackInfo ci){
		this.doPostRenderCleanup();
	}
}
