package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.uncandango.alltheleaks.diag.common.mods.minecraft.EntitySectionCME;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;
import java.util.stream.Stream;

@Mixin(PersistentEntitySectionManager.class)
public class PersistentEntitySectionManagerMixin<T extends EntityAccess> {
	@WrapOperation(method = "lambda$updateChunkStatus$6", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;forEach(Ljava/util/function/Consumer;)V"))
	private void logUpdateChunkStatus(Stream instance, Consumer<? super T> consumer, Operation<Void> original){
		if (ServerLifecycleHooks.getCurrentServer().isSameThread()) {
			EntitySectionCME.SHOULD_LOG.set(true);
			original.call(instance,consumer);
			EntitySectionCME.SHOULD_LOG.set(false);
		} else {
			original.call(instance,consumer);
		}
	}
}
