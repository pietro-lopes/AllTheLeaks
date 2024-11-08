package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.IngredientDedupe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.stream.Stream;

@Mixin(Ingredient.class)
public class IngredientMixin {

	@WrapMethod(method = "fromValues")
	private static Ingredient grabIngredient(Stream<? extends Ingredient.Value> stream, Operation<Ingredient> original) {
		var origIngredient = original.call(stream);
		if (!origIngredient.isEmpty()) {
			var dedupedIngredient = IngredientDedupe.intern(origIngredient);
			//noinspection ConstantValue
			if (dedupedIngredient != origIngredient && (Object) dedupedIngredient instanceof IngredientAccessor accessor) {
//				JsonElement aJson = null;
//				if (origIngredient != null) {
//					aJson = Ingredient.CODEC.encodeStart(JsonOps.INSTANCE, origIngredient).result().orElse(null);
//				}
//				JsonElement bJson = null;
//				if (dedupedIngredient != null) {
//					bJson = Ingredient.CODEC.encodeStart(JsonOps.INSTANCE, dedupedIngredient).result().orElse(null);
//				}
//				var jsonEquality = Objects.equals(aJson, bJson);
//				if (!jsonEquality) {
//					AllTheLeaks.LOGGER.warn("Ingredient: {} from class {} is not equal to: {} from class {}", origIngredient, origIngredient.getClass(), dedupedIngredient, dedupedIngredient.getClass());
//					AllTheLeaks.LOGGER.warn("Jsons: {} vs {}", aJson, bJson);
//				}
				// KubeJS bug is populating itemstacks with Air, this is a workaround to force a re-calculation
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
