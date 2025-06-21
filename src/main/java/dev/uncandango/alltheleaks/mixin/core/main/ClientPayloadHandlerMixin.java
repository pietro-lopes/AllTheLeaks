package dev.uncandango.alltheleaks.mixin.core.main;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handlers.ClientPayloadHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientPayloadHandler.class)
public class ClientPayloadHandlerMixin {

	@ModifyArg(method = "handle(Lnet/neoforged/neoforge/network/payload/FrozenRegistrySyncCompletedPayload;Lnet/neoforged/neoforge/network/handling/IPayloadContext;)V", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/registries/RegistryManager;applySnapshot(Ljava/util/Map;ZZ)Ljava/util/Set;"), index = 2)
	private static boolean fixIsLocal(boolean isLocalWorld) {
		return Minecraft.getInstance().isSingleplayer();
	}
}
