package dev.uncandango.alltheleaks.mixin.core.main;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

@Pseudo
@Mixin(value = GeoArmorRenderer.class, remap = false)
public class GeoArmorRendererMixin<T extends Item & GeoItem> {
    @Shadow
    protected Entity currentEntity;

    @Inject(method = "renderToBuffer", at = @At("TAIL"), remap = true)
    private void atl$clearEntity(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, CallbackInfo ci) {
        this.currentEntity = null;
    }
}
