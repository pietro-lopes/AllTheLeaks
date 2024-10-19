package dev.uncandango.alltheleaks.mixin.core.main;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.MinimapProcessor;

@Pseudo
@Mixin(value = XaeroMinimapSession.class, remap = false)
public abstract class XaeroMinimapSessionMixin {

    @Shadow
    public abstract MinimapProcessor getMinimapProcessor();

    @Inject(method = "cleanup", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V"))
    private void atl$clearRadar(CallbackInfo ci) {
        this.getMinimapProcessor().getEntityRadar().setLastRenderViewEntity(null);
        var it = this.getMinimapProcessor().getEntityRadar().getRadarListsIterator();
        while (it.hasNext()) {
            var value = it.next();
            value.getEntities().clear();
        }
    }
}
