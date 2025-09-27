package dev.uncandango.alltheleaks.mixin.core.main;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.fabricmc.fabric.impl.networking.server.ServerPlayNetworkAddon;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerPlayNetworkHandlerMixinSquared {
	@TargetHandler(
		mixin = "net.fabricmc.fabric.mixin.networking.ServerPlayNetworkHandlerMixin",
		name = "initAddon"
	)
	@WrapWithCondition(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/impl/networking/server/ServerPlayNetworkAddon;lateInit()V", remap = false))
	private boolean preventInitIfFakePlayer(ServerPlayNetworkAddon addon){
		return (!(((Object) this) instanceof FakePlayer.FakePlayerNetHandler));
	}
}
