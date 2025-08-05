package dev.uncandango.alltheleaks.mixin.core.main;

import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SizedIngredient.class)
public class SizedIngredientMixin {
	@CompatibleHashes(values = {-2032821999})
	@ModifyExpressionValue(method = "getItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;getItems()[Lnet/minecraft/world/item/ItemStack;"))
	private ItemStack[] atl$safeArrayStack(ItemStack[] original){
		return ArrayUtils.clone(original);
	}
}
