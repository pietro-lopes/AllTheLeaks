package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.diag.common.mods.minecraft.EntitySectionCME;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ConcurrentModificationException;

@Mixin(EntitySection.class)
public class EntitySectionMixin<T extends EntityAccess> {
	@Inject(method = "remove", at = @At("HEAD"))
	private void shouldLogRemoved(T entity, CallbackInfoReturnable<Boolean> cir) {
		if (ServerLifecycleHooks.getCurrentServer().isSameThread() && EntitySectionCME.SHOULD_LOG.get()){
			AllTheLeaks.LOGGER.error("Tried to remove entity " + entity + " while looping!", new ConcurrentModificationException());
		}
	}

	@Inject(method = "add", at = @At("HEAD"))
	private void shouldLogAdded(T entity, CallbackInfo ci) {
		if (ServerLifecycleHooks.getCurrentServer().isSameThread() && EntitySectionCME.SHOULD_LOG.get()){
			AllTheLeaks.LOGGER.error("Tried to add entity " + entity + " while looping!", new ConcurrentModificationException());
		}
	}
}
