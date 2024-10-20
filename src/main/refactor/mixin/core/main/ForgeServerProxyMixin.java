package dev.uncandango.alltheleaks.mixin.core.main;

import com.seibel.distanthorizons.forge.ForgeServerProxy;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.ChunkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.uncandango.alltheleaks.mixin.ServerLevelWrapperExtension.atl$checkIfUnloaded;

@Pseudo
@Mixin(value = ForgeServerProxy.class, remap = false)
public class ForgeServerProxyMixin {

    @Inject(method = "serverChunkSaveEvent", at = @At("HEAD"), cancellable = true)
    void atl$cancelChunkSave(ChunkEvent.Unload event, CallbackInfo ci) {
        if (event.getLevel() instanceof ServerLevel level) {
            if (atl$checkIfUnloaded(level)) {
                ci.cancel();
            }
        }
    }
}
