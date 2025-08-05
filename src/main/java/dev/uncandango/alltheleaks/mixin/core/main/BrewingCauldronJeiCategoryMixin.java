package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.integration.BrewingCauldronJeiCategory;
import mod.motivationaldragon.potionblender.recipes.BrewingCauldronRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = BrewingCauldronJeiCategory.class, remap = false)
public class BrewingCauldronJeiCategoryMixin {

	@CompatibleHashes(values = {-1847586359})
	@WrapOperation(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lmod/motivationaldragon/potionblender/recipes/BrewingCauldronRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V", at = @At(value = "INVOKE", target = "Lmezz/jei/api/gui/builder/IRecipeSlotBuilder;addIngredients(Lnet/minecraft/world/item/crafting/Ingredient;)Lmezz/jei/api/gui/builder/IIngredientAcceptor;"))
	private IIngredientAcceptor<?> atl$safeIngredient(IRecipeSlotBuilder instance, Ingredient ingredient, Operation<IIngredientAcceptor<?>> original, @Local(argsOnly = true) BrewingCauldronRecipe recipe){
		List<ItemStack> stackList = new ArrayList<>();
		for (ItemStack stack : ingredient.getItems()) {
			var copy = stack.copy();
			if ((copy.getItem() instanceof PotionItem) && recipe.usePotionMeringRules()) {
				copy.setHoverName(Component.translatable(Constants.MOD_ID + ".recipe.potion_wildcard_names"));
				copy.enchant(null, 0);
			}
			stackList.add(copy);
		}
		return instance.addItemStacks(stackList);
	}
}
