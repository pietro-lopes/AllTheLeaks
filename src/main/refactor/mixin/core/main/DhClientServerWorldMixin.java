package dev.uncandango.alltheleaks.mixin.core.main;

import com.seibel.distanthorizons.core.level.DhClientServerLevel;
import com.seibel.distanthorizons.core.world.DhClientServerWorld;
import com.seibel.distanthorizons.core.wrapperInterfaces.world.ILevelWrapper;
import com.seibel.distanthorizons.core.wrapperInterfaces.world.IServerLevelWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashSet;
import java.util.Iterator;

@Pseudo
@Mixin(value = DhClientServerWorld.class, remap = false)
public class DhClientServerWorldMixin {

    @Inject(method = "unloadLevel", at = @At(value = "INVOKE", target = "Ljava/util/HashMap;remove(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 1))
    void atl$unloadClientWorld(ILevelWrapper wrapper, CallbackInfo ci) {
        wrapper.onUnload();
    }

    @Inject(method = "close", at = @At(value = "INVOKE", target = "Lcom/seibel/distanthorizons/core/level/DhClientServerLevel;close()V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    void atl$unloadClientAtWorldExit(CallbackInfo ci, HashSet<DhClientServerLevel> levelsToClose, Iterator<DhClientServerLevel> i, DhClientServerLevel level, IServerLevelWrapper serverLevelWrapper) {
        // something
        var clientWrapper = level.getClientLevelWrapper();
        if (clientWrapper != null) clientWrapper.onUnload();
    }
}
