package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import mods.railcraft.integrations.jei.DefaultRecipeWrapper;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;

@Mixin(value = DefaultRecipeWrapper.class, remap = false)
public class DefaultRecipeWrapperMixin {

	@CompatibleHashes(values = {-1828317208})
	@ModifyExpressionValue(method = "lambda$setRecipe$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;getItems()[Lnet/minecraft/world/item/ItemStack;", remap = true))
	private static ItemStack[] atl$safeStack(ItemStack[] original){
		return Arrays.stream(original).map(ItemStack::copy).toArray(ItemStack[]::new);
	}
}
