package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.AllTheLeaks;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundSetPlayerTeamPacket.class)
public class ClientboundSetPlayerTeamPacketMixin {
	@Inject(method = {"<init>(Ljava/lang/String;ILjava/util/Optional;Ljava/util/Collection;)V","<init>(Lnet/minecraft/network/RegistryFriendlyByteBuf;)V"}, at = @At("TAIL"))
	private void printStackTrace(CallbackInfo ci){
		AllTheLeaks.LOGGER.warn("Dumping stacktrace of ClientboundSetPlayerTeamPacket...");
		Thread.dumpStack();
	}

}
