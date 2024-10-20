package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.utils.Storage;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class ConnectionMixin {
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    void atl$grabConnections(PacketFlow arg, CallbackInfo ci) {
        Storage.allConnections.add(((Connection) (Object) this));
    }
}
