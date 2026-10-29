package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import it.hurts.octostudios.octolib.module.particle.trail.EntityTrailProvider;
import it.hurts.octostudios.perception.common.modules.trail.misc.wrapper.base.TrailWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TrailWrapper.class)
public abstract class TrailWrapperMixin<T extends Entity> extends EntityTrailProvider<T> {
	public TrailWrapperMixin(T entity) {
		super(entity);
	}

	@ModifyExpressionValue(method = "isTrailAlive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isAlive()Z"))
	private boolean checkIfSameLevel(boolean original){
		if (original) {
			return this.entity.level() == Minecraft.getInstance().level;
		}
		return original;
	}
}
