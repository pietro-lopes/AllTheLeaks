package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.fabricmc.fabric.impl.networking.AbstractNetworkAddon;
import net.fabricmc.fabric.impl.networking.GlobalReceiverRegistry;
import net.fabricmc.fabric.impl.networking.server.ServerPlayNetworkAddon;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ServerPlayNetworkAddon.class, remap = false)
public class ServerPlayNetworkAddonMixin {
	@Shadow
	@Final
	private ServerGamePacketListenerImpl handler;

	@WrapWithCondition(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/impl/networking/GlobalReceiverRegistry;startSession(Lnet/fabricmc/fabric/impl/networking/AbstractNetworkAddon;)V"))
	private boolean preventFakePlayerRegister(GlobalReceiverRegistry instance, AbstractNetworkAddon<Object> addon){
		return (!(this.handler instanceof FakePlayer.FakePlayerNetHandler));
	}
}
