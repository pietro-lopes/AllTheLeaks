package dev.uncandango.alltheleaks.mixin.core.main;

import com.klikli_dev.occultism.registry.OccultismCapabilities;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = OccultismCapabilities.class, remap = false)
public class OccultismCapabilitiesMixin {
	@Inject(method = "onPlayerClone", at = @At("TAIL"))
	private static void invalidateCaps(PlayerEvent.Clone event, CallbackInfo ci){
		if (event.isWasDeath()) event.getOriginal().invalidateCaps();
	}
}
