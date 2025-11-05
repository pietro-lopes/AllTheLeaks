package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

@Mixin(value = GeoArmorRenderer.class, remap = false)
public abstract class GeoArmorRendererMixin implements UpdateableLevel<GeoArmorRenderer<?>>{
	@Shadow
	protected Entity currentEntity;

	@Inject(method = "<init>(Lsoftware/bernie/geckolib/model/GeoModel;)V", at = @At("RETURN"))
	private void registerInstance(CallbackInfo ci) {
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		this.currentEntity = null;
	}
}
