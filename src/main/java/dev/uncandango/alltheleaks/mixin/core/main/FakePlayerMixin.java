package dev.uncandango.alltheleaks.mixin;

import com.mojang.authlib.GameProfile;
import dev.uncandango.alltheleaks.leaks.common.mods.neoforge.UntrackedIssue001;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FakePlayer.class, remap = false)
public class FakePlayerMixin {
	@Inject(method = "<init>", at = @At(value = "RETURN"))
	private void atl$registerAnyFakePlayer(ServerLevel level, GameProfile name, CallbackInfo ci){
		if ((Object)this.getClass() != FakePlayer.class) {
			UntrackedIssue001.registerFakePlayer(level, name, (FakePlayer)(Object)this);
		}
	}
}
