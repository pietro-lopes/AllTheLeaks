package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = {"com.tiviacz.travelersbackpack.handlers.NeoForgeEventHandler"}, remap = false)
public class NeoForgeEventHandlerMixin {
	@WrapOperation(method = "playerClone", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;revive()V"))
	private static void atl$properReviveCaps(Player instance, Operation<Void> original){
		instance.reviveCaps();
	}

	@Inject(method = "playerClone", at = @At(value = "TAIL"))
	private static void atl$invalidateCaps(PlayerEvent.Clone event, CallbackInfo ci){
		event.getOriginal().invalidateCaps();
	}
}
