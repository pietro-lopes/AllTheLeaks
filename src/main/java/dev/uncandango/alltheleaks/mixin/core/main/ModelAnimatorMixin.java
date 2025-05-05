package dev.uncandango.alltheleaks.mixin.core.main;

import com.github.L_Ender.lionfishapi.client.model.Animations.ModelAnimator;
import com.github.L_Ender.lionfishapi.server.animation.IAnimatedEntity;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelAnimator.class)
public abstract class ModelAnimatorMixin implements UpdateableLevel<ModelAnimator> {
	@Shadow
	public abstract void update(IAnimatedEntity entity);

	@Inject(method = "<init>()V", at = @At("RETURN"))
	private void registerInstance(CallbackInfo ci) {
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		this.update(null);
	}
}
