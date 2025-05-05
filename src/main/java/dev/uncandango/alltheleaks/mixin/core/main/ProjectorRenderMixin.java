package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import stepsword.mahoutsukai.render.ProjectorRenderer;

@Mixin(ProjectorRenderer.class)
public class ProjectorRenderMixin {

	@WrapMethod(method = "renderProjector")
	private static void skipRing(boolean enchant, PoseStack matrix, MultiBufferSource buffer, int loadImage, boolean showCircle, boolean gif_layer, float gif_frame, float size, float r, float g, float b, float a, float ringangle, float height, int rune, boolean showRing, float ring, float prepitch, float preyaw, float orbit, float pitch, float yaw, float rotation, float offsetx, float offsety, float offsetz, float headpitch, Operation<Void> original){
		original.call(enchant,matrix,buffer,loadImage,showCircle,gif_layer,gif_frame,size,r,g,b,a,ringangle,height,rune,false,ring,prepitch,preyaw,orbit,pitch,yaw,rotation,offsetx,offsety,offsetz,headpitch);
	}
}
