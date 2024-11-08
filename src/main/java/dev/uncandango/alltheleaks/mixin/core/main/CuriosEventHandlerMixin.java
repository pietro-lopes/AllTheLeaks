package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import top.theillusivec4.curios.common.event.CuriosEventHandler;

@Mixin(value = CuriosEventHandler.class, remap = false)
public class CuriosEventHandlerMixin {
	@WrapOperation(method = "playerClone", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;revive()V"))
	private void atl$cancelRevive(Player instance, Operation<Void> original){
		// NO-OP
	}
}
