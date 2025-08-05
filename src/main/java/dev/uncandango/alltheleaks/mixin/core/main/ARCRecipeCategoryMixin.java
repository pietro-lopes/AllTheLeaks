package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import wayoftime.bloodmagic.compat.jei.arc.ARCRecipeCategory;

import java.util.Arrays;

@Mixin(ARCRecipeCategory.class)
public class ARCRecipeCategoryMixin {

	@CompatibleHashes(values = {1243424396})
	@ModifyExpressionValue(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lwayoftime/bloodmagic/recipe/RecipeARC;Lmezz/jei/api/recipe/IFocusGroup;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;getItems()[Lnet/minecraft/world/item/ItemStack;"))
	private ItemStack[] atl$safeStack(ItemStack[] original){
		return Arrays.stream(original).map(ItemStack::copy).toArray(ItemStack[]::new);
	}
}
