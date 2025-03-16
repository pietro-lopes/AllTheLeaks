package dev.uncandango.alltheleaks.mixin.core.main;

import com.bobmowzie.mowziesmobs.client.render.entity.MowzieGeoEntityRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderUmvuthi;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import software.bernie.geckolib.model.GeoModel;

@Mixin(value = RenderUmvuthi.class, remap = false)
public abstract class RenderUmvuthiMixin extends MowzieGeoEntityRenderer<EntityUmvuthi> {
	@Shadow
	private EntityUmvuthi entity;

	protected RenderUmvuthiMixin(EntityRendererProvider.Context renderManager, GeoModel<EntityUmvuthi> modelProvider) {
		super(renderManager, modelProvider);
	}

	@Override
	public void doPostRenderCleanup() {
		super.doPostRenderCleanup();
		this.entity = null;
	}
}
