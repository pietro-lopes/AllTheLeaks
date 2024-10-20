package dev.uncandango.alltheleaks.mixin.core.main;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoObjectRenderer;

@Pseudo
@Mixin(value = GeoObjectRenderer.class, remap = false)
public class GeoObjectRendererMixin<T extends GeoAnimatable> {

    @Shadow
    protected T animatable;

    @Inject(method = "render", at = @At("TAIL"))
    private void atl$clearEntity(PoseStack poseStack, T animatable, MultiBufferSource bufferSource, RenderType renderType, VertexConsumer buffer, int packedLight, CallbackInfo ci) {
        this.animatable = null;
    }
}
