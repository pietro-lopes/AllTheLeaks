package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import guideme.document.block.LytSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;

@Mixin(LytSlot.class)
public class LytSlotMixin {
	@CompatibleHashes(values = {-270253667})
	@ModifyExpressionValue(method = "<init>(Lnet/minecraft/world/item/crafting/Ingredient;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;getItems()[Lnet/minecraft/world/item/ItemStack;"))
	private ItemStack[] atl$safeCopyStacks(ItemStack[] original){
		return Arrays.stream(original).map(ItemStack::copy).toArray(ItemStack[]::new);
	}
}
