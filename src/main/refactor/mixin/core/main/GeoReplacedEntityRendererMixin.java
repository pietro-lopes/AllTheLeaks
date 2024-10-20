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
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

@Pseudo
@Mixin(value = GeoReplacedEntityRenderer.class, remap = false)
public class GeoReplacedEntityRendererMixin<E extends Entity, T extends GeoAnimatable> {

    @Shadow
    protected E currentEntity;

    @Inject(method = "render", at = @At("TAIL"), remap = true)
    private void atl$clearEntity(E entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        this.currentEntity = null;
    }
}
