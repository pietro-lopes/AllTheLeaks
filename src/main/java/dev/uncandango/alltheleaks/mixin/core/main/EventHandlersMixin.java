package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.gigaherz.toolbelt.ConfigData;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "dev.gigaherz.toolbelt.slot.BeltExtensionSlot$EventHandlers", remap = false)
public class EventHandlersMixin {

	@Inject(method = "playerClone", at = @At("TAIL"))
	private void invalidateCaps(PlayerEvent.Clone event, CallbackInfo ci) {
		if (ConfigData.customBeltSlotEnabled) {
			event.getOriginal().invalidateCaps();
		}
	}

	@WrapOperation(method = "playerClone", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;revive()V"))
	private void properReviveCaps(Player instance, Operation<Void> original) {
		instance.reviveCaps();
	}
}
