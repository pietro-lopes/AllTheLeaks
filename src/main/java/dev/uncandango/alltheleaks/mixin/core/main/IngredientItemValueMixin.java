package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Ingredient.ItemValue.class)
public class IngredientItemValueMixin {
	@ModifyExpressionValue(method = "getItems", at = @At(value = "FIELD", target = "Lnet/minecraft/world/item/crafting/Ingredient$ItemValue;item:Lnet/minecraft/world/item/ItemStack;"))
	private ItemStack atl$safeStack(ItemStack original){
		return original.copy();
	}
}
