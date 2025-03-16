package dev.uncandango.alltheleaks.mixin.core.main;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderGeomancyBase;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Mixin(value = RenderGeomancyBase.class, remap = false)
public abstract class RenderGeomancyBaseMixin<T extends EntityGeomancyBase & GeoEntity> extends GeoEntityRenderer<T> {
	@Shadow
	private T entity;

	public RenderGeomancyBaseMixin(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
		super(renderManager, model);
	}

	@Override
	public void doPostRenderCleanup() {
		super.doPostRenderCleanup();
		this.entity = null;
	}
}
