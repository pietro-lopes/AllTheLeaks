package dev.uncandango.alltheleaks.mixin.core.main;

import com.seibel.distanthorizons.core.wrapperInterfaces.world.IServerLevelWrapper;
import dev.uncandango.alltheleaks.mixin.ServerLevelWrapperExtension;
import loaderCommon.forge.com.seibel.distanthorizons.common.wrappers.world.ServerLevelWrapper;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ConcurrentHashMap;

@Pseudo
@Mixin(value = ServerLevelWrapper.class, remap = false)
public abstract class ServerLevelWrapperMixin implements ServerLevelWrapperExtension {

    @Shadow
    @Final
    private static ConcurrentHashMap<ServerLevel, ServerLevelWrapper> LEVEL_WRAPPER_BY_SERVER_LEVEL;

    @Mutable
    @Shadow
    @Final
    ServerLevel level;

    @Invoker("<init>")
    static ServerLevelWrapper newServerLevelWrapper(ServerLevel level) {
        throw new AssertionError("Failed to apply mixin!");
    }

    @Unique
    private static ServerLevelWrapper atl$createWrapper(ServerLevel level) {
        return ServerLevelWrapperExtension.atl$checkIfUnloaded(level) ? null : LEVEL_WRAPPER_BY_SERVER_LEVEL.computeIfAbsent(level, ServerLevelWrapperMixin::newServerLevelWrapper);
    }

    @Inject(method = "getWrapper", at = @At(value = "HEAD"), cancellable = true)
    private static void atl$debugAtStart(ServerLevel level, CallbackInfoReturnable<IServerLevelWrapper> cir) {
        var wrapper = LEVEL_WRAPPER_BY_SERVER_LEVEL.getOrDefault(level, atl$createWrapper(level));
        cir.setReturnValue(wrapper);
    }

    @Inject(method = "onUnload", at = @At(value = "HEAD"))
    private void atl$debugAtUnloadStart(CallbackInfo ci) {
        atl$unloadedLevels.add(this.level);
    }
}
