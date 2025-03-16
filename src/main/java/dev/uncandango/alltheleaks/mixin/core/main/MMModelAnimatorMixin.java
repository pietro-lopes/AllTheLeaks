package dev.uncandango.alltheleaks.mixin.core.main;

import com.bobmowzie.mowziesmobs.client.model.tools.MMModelAnimator;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MMModelAnimator.class, remap = false)
public class MMModelAnimatorMixin implements UpdateableLevel<MMModelAnimator>{
	@Shadow
	private IAnimatedEntity entity;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void atl$registerInstance(CallbackInfo ci) {
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		if (this.entity instanceof Entity realEntity && level != null && realEntity.level() != level) {
			entity = (IAnimatedEntity) realEntity.getType().create(level);
		}
	}
}
