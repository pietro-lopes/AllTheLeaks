package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.leaks.client.mods.badpackets.UntrackedIssue001;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
	@Inject(method = "close", at = @At("TAIL"))
	private void atl$handleCloseForBadPackets(CallbackInfo ci) {
		UntrackedIssue001.clearPacketsFromHandler((ClientPacketListener) (Object) this);
	}
}
