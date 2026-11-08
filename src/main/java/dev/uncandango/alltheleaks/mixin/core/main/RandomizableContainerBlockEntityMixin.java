package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RandomizableContainerBlockEntity.class)
public class RandomizableContainerBlockEntityMixin {
	@ModifyExpressionValue(method = "isEmpty", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/RandomizableContainerBlockEntity;getItems()Lnet/minecraft/core/NonNullList;"))
	private NonNullList<ItemStack> atl$allMatchStream(NonNullList<ItemStack> original, @Cancellable CallbackInfoReturnable<Boolean> cir) {
		for (var item : original) {
			if (!item.isEmpty()){
				cir.setReturnValue(false);
				return original;
			}
		}
		cir.setReturnValue(true);
		return original;
	}
}
