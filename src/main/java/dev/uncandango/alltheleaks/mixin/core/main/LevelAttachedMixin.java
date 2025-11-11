package dev.uncandango.alltheleaks.mixin.core.main;

import dev.engine_room.flywheel.lib.util.LevelAttached;
import dev.uncandango.alltheleaks.leaks.client.mods.flywheel.UntrackedIssue001;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelAttached.class)
public class LevelAttachedMixin<T> {

//    @ModifyExpressionValue(method = "lambda$new$0", at = @At(value = "INVOKE", target = "Lcom/google/common/cache/RemovalNotification;getValue()Ljava/lang/Object;"))
//    private static Object getUnloadedLevel(Object original) {
//        if (original instanceof VisualizationManagerImplAccessor vm) {
//            var level = vm.atl$getLevel();
//            if (level instanceof ClientLevel cl) {
//                AllTheLeaks.LOGGER.info("Unloading {} ({}) with {}",
//                        level.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(level)),
//                        cl.dimension().location(),
//                        vm.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(vm)));
//                if (!atl$unloadedLevels.contains(System.identityHashCode(level))) {
//                    AllTheLeaks.LOGGER.warn("This level unload was NOT received yet via event");
//                }
//            }
//        }
//        return original;
//    }

	@Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void cancelIfOldWorld(LevelAccessor level, CallbackInfoReturnable<T> cir) {
        if (level instanceof ClientLevel cl && UntrackedIssue001.lastUnloadedLevelHash.get() == System.identityHashCode(cl)) {
			// AllTheLeaks.LOGGER.info("Tried to request VisualizationManager using an unloaded level {}@{}", level.getClass().getSimpleName(), Integer.toHexString(System.identityHashCode(level)));
			cir.setReturnValue(null);
        }
    }

//    @Mixin(VisualizationManagerImpl.class)
//    public interface VisualizationManagerImplAccessor {
//        @Accessor("level")
//        LevelAccessor atl$getLevel();
//    }
//
//    @Mixin(targets = "dev.engine_room.flywheel.lib.util.LevelAttached$1")
//    public static class LevelAttachedMixin2 {
//
//        @Inject(method = "load(Lnet/minecraft/world/level/LevelAccessor;)Ljava/lang/Object;", at = @At("HEAD"), cancellable = true)
//        private void grabLevelBeingLoaded(LevelAccessor key, CallbackInfoReturnable<?> cir) {
//            if (LevelAttachedExtension.atl$unloadedLevels.contains(System.identityHashCode(key))) {
//                var ex = new IllegalStateException();
//                var message = String.format("Tried to generate object from %s@%s (%s)", key.getClass().getSimpleName(),
//                        Integer.toHexString(System.identityHashCode(key)),
//                        ((ClientLevel) key).dimension().location());
//                AllTheLeaks.LOGGER.error(message, ex);
//				cir.setReturnValue(null);
//            }
//        }
//    }
}
