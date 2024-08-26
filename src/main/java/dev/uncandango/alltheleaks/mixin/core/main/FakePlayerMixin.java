package dev.uncandango.alltheleaks.mixin.core.main;

import net.neoforged.neoforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = FakePlayer.class, remap = false)
public class FakePlayerMixin {
//	@Inject(method = "<init>", at = @At(value = "RETURN"))
//	private void atl$registerAnyFakePlayer(ServerLevel level, GameProfile name, CallbackInfo ci){
//		if ((Object)this.getClass() != FakePlayer.class) {
//			Issue1487.registerFakePlayer(level, name, (FakePlayer)(Object)this);
//		}
//	}
}
