package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.uncandango.alltheleaks.AllTheLeaks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.stream.Stream;

import static dev.uncandango.alltheleaks.fix.common.minecraft.IngredientDedupe.INGREDIENT_CACHE;

@Mixin(Ingredient.class)
public class IngredientMixin {

	@WrapMethod(method = "fromValues")
	private static Ingredient grabIngredient(Stream<? extends Ingredient.Value> stream, Operation<Ingredient> original) {
		var origIngredient = original.call(stream);
		if (!origIngredient.isEmpty()) {
			var dedupedIngredient = INGREDIENT_CACHE.get().asMap().putIfAbsent(origIngredient, origIngredient);
			//noinspection ConstantValue
			if (dedupedIngredient != null && (Object) dedupedIngredient instanceof IngredientAccessor accessor) {
				accessor.setItemStacks(null);
				return dedupedIngredient;
			}
		}
		return origIngredient;
	}

	@Mixin(Ingredient.class)
	public interface IngredientAccessor {
		@Accessor("itemStacks")
		void setItemStacks(ItemStack[] itemStacks);
	}
}
