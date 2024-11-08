package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tiviacz.travelersbackpack.handlers.NeoForgeEventHandler;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NeoForgeEventHandler.class)
public class NeoForgeEventHandlerMixin {
	@WrapOperation(method = "playerClone", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;revive()V"))
	private static void atl$cancelRevive(Player instance, Operation<Void> original) {
		// NO-OP
	}
}
