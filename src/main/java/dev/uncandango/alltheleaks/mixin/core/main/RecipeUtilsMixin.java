package dev.uncandango.alltheleaks.mixin.core.main;

import com.portingdeadmods.nautec.content.recipes.utils.IngredientWithCount;
import com.portingdeadmods.nautec.content.recipes.utils.RecipeUtils;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.stream.Stream;

@Mixin(RecipeUtils.class)
public class RecipeUtilsMixin {
	/**
	 * @author Uncandango
	 * @reason Stop setting count and use SizedIngredient
	 */
	@Overwrite
	public static @NotNull Ingredient iWCToIngredientSaveCount(IngredientWithCount ingredientWithCount) {
		return Ingredient.of(Stream.of(ingredientWithCount.ingredient().getItems()).map(s -> s.copyWithCount(ingredientWithCount.count())));
	}
}
