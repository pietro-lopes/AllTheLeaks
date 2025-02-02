package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.uncandango.alltheleaks.config.ATLProperties;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.IngredientDedupe;
import dev.uncandango.alltheleaks.mixin.Lockable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.stream.Streams;
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
				if (ATLProperties.get().debugItemStackModifications) {
					if (!((Lockable) (Object) dedupedIngredient).isLocked()) {
						Streams.of(dedupedIngredient.getValues()).map(Ingredient.Value::getItems)
							.forEach(stacks -> stacks.forEach(stack -> ((Lockable) (Object) stack).setLocked(true)));
						((Lockable) (Object) dedupedIngredient).setLocked(true);
					}
				}
				// KubeJS bug is populating itemstacks with Air, this is a workaround to force a re-calculation
				// accessor.setItemStacks(null);
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
