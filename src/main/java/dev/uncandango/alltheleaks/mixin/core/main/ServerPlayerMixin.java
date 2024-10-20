package dev.uncandango.alltheleaks.mixin.core.main;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayer.class)
public class ServerPlayerMixin {
	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void atl$clearTriggers(MinecraftServer server, ServerLevel level, GameProfile gameProfile, CallbackInfo ci) {
		if ((Object) this instanceof FakePlayer fakePlayer) {
			fakePlayer.getAdvancements().stopListening();
		}
	}
}
