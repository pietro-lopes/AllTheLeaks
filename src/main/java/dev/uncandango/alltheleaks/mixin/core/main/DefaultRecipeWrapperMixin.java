package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import mods.railcraft.integrations.jei.DefaultRecipeWrapper;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;

@Mixin(DefaultRecipeWrapper.class)
public class DefaultRecipeWrapperMixin {
	@ModifyExpressionValue(method = "lambda$setRecipe$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;getItems()[Lnet/minecraft/world/item/ItemStack;"))
	private static ItemStack[] safeCopy(ItemStack[] original){
		return Arrays.stream(original).map(ItemStack::copy).toArray(ItemStack[]::new);
	}
}
