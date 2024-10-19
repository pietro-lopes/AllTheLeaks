package dev.uncandango.alltheleaks.mixin.core.main;

import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = CapabilityRegistry.EventHandler.class, remap = false)
public class EventHandlerMixin {
	@WrapOperation(method = "playerClone", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;revive()V"))
	private static void atl$properReviveCaps(Player instance, Operation<Void> original){
		instance.reviveCaps();
	}
}
