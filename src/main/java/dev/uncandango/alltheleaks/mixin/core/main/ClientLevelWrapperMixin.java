package dev.uncandango.alltheleaks.mixin.core.main;

import com.seibel.distanthorizons.core.wrapperInterfaces.world.IClientLevelWrapper;
import dev.uncandango.alltheleaks.mixin.ClientLevelWrapperExtension;
import loaderCommon.forge.com.seibel.distanthorizons.common.wrappers.world.ClientLevelWrapper;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ConcurrentHashMap;

@Pseudo
@Mixin(value = ClientLevelWrapper.class, remap = false)
public abstract class ClientLevelWrapperMixin implements ClientLevelWrapperExtension {

    @Shadow
    @Final
    private static ConcurrentHashMap<ClientLevel, ClientLevelWrapper> LEVEL_WRAPPER_BY_CLIENT_LEVEL;

    @Mutable
    @Shadow
    @Final
    private ClientLevel level;

    //    @Inject(method = "<init>", at = @At("TAIL"))
//    private void atl$debugLevelCreation(ClientLevel level, CallbackInfo ci){
//        if (LEVEL_WRAPPER_BY_CLIENT_LEVEL.size() > 1){
//            AllTheLeaks.LOGGER.info("Leak detectado!");
//        }
//    }
//    @Unique
//    private static final AtomicInteger atl$before = new AtomicInteger(0);
    @Invoker("<init>")
    static ClientLevelWrapper newClientLevelWrapper(ClientLevel level) {
        throw new AssertionError("Failed to apply mixin!");
    }

    @Unique
    private static ClientLevelWrapper atl$createWrapper(ClientLevel level) {
        return atl$unloadedLevels.contains(level) ? null : LEVEL_WRAPPER_BY_CLIENT_LEVEL.computeIfAbsent(level, ClientLevelWrapperMixin::newClientLevelWrapper);
    }

    @Inject(method = "getWrapperIgnoringOverride", at = @At(value = "HEAD"), cancellable = true)
    private static void atl$debugAtStart(ClientLevel level, CallbackInfoReturnable<IClientLevelWrapper> cir) {
        var wrapper = LEVEL_WRAPPER_BY_CLIENT_LEVEL.get(level);
        if (wrapper != null) {
            cir.setReturnValue(wrapper);
            return;
        } else wrapper = atl$createWrapper(level);
        if (wrapper != null) LEVEL_WRAPPER_BY_CLIENT_LEVEL.put(level, wrapper);
        cir.setReturnValue(wrapper);
    }

    @Inject(method = "onUnload", at = @At(value = "HEAD"))
    private void atl$debugAtUnloadStart(CallbackInfo ci) {
        atl$unloadedLevels.add(this.level);
//        if (!LEVEL_WRAPPER_BY_CLIENT_LEVEL.containsKey(this.level)) {
//            AllTheLeaks.LOGGER.info("Tried to unload a non existent level!");
//        }
    }

//    @Inject(method = "getWrapperIgnoringOverride", at = @At(value = "TAIL"))
//    private static void atl$debugAtEnd(ClientLevel level, CallbackInfoReturnable<IClientLevelWrapper> cir){
//        if (atl$before.get() < LEVEL_WRAPPER_BY_CLIENT_LEVEL.size()) {
//            AllTheLeaks.LOGGER.info("Level adicionado!");
//            if (LEVEL_WRAPPER_BY_CLIENT_LEVEL.size() > 2) {
//                AllTheLeaks.LOGGER.info("Leak detectado!");
//                for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
//                    System.out.println(ste + "\n");
//                }
//            }
//        }
//    }
}
