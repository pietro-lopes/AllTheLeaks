package dev.uncandango.alltheleaks.mixin.core.main;

import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "com.direwolf20.justdirethings.client.blockentityrenders.InventoryHolderBER")
public class InventoryHolderBERMixin {
	@Shadow
	public static AbstractClientPlayer mockPlayer;

	@WrapOperation(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/direwolf20/justdirethings/client/blockentityrenders/InventoryHolderBER;createMockPlayer(Lcom/direwolf20/justdirethings/common/blockentities/InventoryHolderBE;)Lnet/minecraft/client/player/AbstractClientPlayer;"))
	private void myHandler(AbstractClientPlayer value, Operation<Void> original, @Local(argsOnly = true) BlockEntity be){
		if (mockPlayer == null || !mockPlayer.getUUID().equals(((BaseMachineBE)be).placedByUUID)) {
			original.call(value);
		}
	}
}
