package dev.uncandango.alltheleaks.mixin.core.main;

import lol.bai.badpackets.impl.handler.ClientPacketHandler;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
	@Inject(method = "close", at = @At("TAIL"))
	private void atl$handleCloseForBadPackets(CallbackInfo ci){
		if (this instanceof ClientPacketHandler.Holder holder) {
			holder.badpackets_getHandler().onDisconnect();
		}
	}
}
