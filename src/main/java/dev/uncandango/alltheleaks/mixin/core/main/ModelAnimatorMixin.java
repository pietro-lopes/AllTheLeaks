package dev.uncandango.alltheleaks.mixin.core.main;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModelAnimator.class, remap = false)
public class ModelAnimatorMixin implements UpdateableLevel<ModelAnimator> {
	@Shadow
	private IAnimatedEntity entity;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void atl$registerInstance(CallbackInfo ci){
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		if (this.entity instanceof Entity realEntity && level != null) {
			if (realEntity.level() != level) {
				entity = (IAnimatedEntity) realEntity.getType().create(level);
			}
		}
	}
}
