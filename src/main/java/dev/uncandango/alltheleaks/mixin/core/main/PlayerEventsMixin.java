package dev.uncandango.alltheleaks.mixin.core.main;

import com.stal111.forbidden_arcanus.common.event.PlayerEvents;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerEvents.class, remap = false)
public class PlayerEventsMixin {
	@Inject(method = "onPlayerClone", at = @At("TAIL"))
	private static void invalidateCaps(PlayerEvent.Clone event, CallbackInfo ci) {
		if (event.isWasDeath()) {
			event.getOriginal().invalidateCaps();
		}
	}
}
