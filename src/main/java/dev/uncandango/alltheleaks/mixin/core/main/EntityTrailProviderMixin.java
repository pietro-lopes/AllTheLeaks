package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import it.hurts.octostudios.octolib.module.particle.trail.EntityTrailProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityTrailProvider.class)
public class EntityTrailProviderMixin<T extends Entity> {
	@Shadow
	public T entity;

	@ModifyExpressionValue(method = "isTrailAlive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isAlive()Z"))
	private boolean checkIfSameLevel(boolean original){
		if (original) {
			return this.entity.level() == Minecraft.getInstance().level;
		}
		return original;
	}
}
