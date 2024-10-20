package dev.uncandango.alltheleaks.mixin.core.main;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Pseudo
@Mixin(value = GeoEntityRenderer.class, remap = false)
public class GeoEntityRendererMixin<T extends Entity & GeoAnimatable> {


    @Shadow
    protected T animatable;

    @SuppressWarnings("MixinAnnotationTarget")
    @Inject(method = "m_7392_", at = @At("TAIL"))
    private void atl$clearEntity(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        this.animatable = null;
    }
}
