package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.config.ATLProperties;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.IngredientDedupe;
import dev.uncandango.alltheleaks.mixin.Lockable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.stream.Streams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.stream.Stream;

@Mixin(Ingredient.class)
public class IngredientMixin {

	@WrapMethod(method = "fromValues")
	private static Ingredient grabIngredient(Stream<? extends Ingredient.Value> stream, Operation<Ingredient> original) {
		var origIngredient = original.call(stream);
		if (!origIngredient.isEmpty() && !origIngredient.isCustom()) {
			var dedupedIngredient = IngredientDedupe.intern(origIngredient);
			//noinspection ConstantValue
			if (dedupedIngredient != origIngredient && (Object) dedupedIngredient instanceof IngredientAccessor accessor) {
				if (ATLProperties.get().debugItemStackModifications) {
					var origStacks = origIngredient.getItems();
					var dedupeStacks = dedupedIngredient.getItems();
					if (origStacks.length != dedupeStacks.length) {
						AllTheLeaks.LOGGER.error("Ingredient stack length is not the same");
					} else {
						var isTag = false;
						var values = origIngredient.getValues();
						if (values.length > 0 && values[0] instanceof Ingredient.TagValue) {
							isTag = true;
						}
						if (!isTag) {
							for (int i = 0; i < origStacks.length; i++) {
								var origStack = origStacks[i];
								var dedupeStack = dedupeStacks[i];
								if (!ItemStack.isSameItemSameComponents(origStack, dedupeStack)) {
									var error = new IllegalArgumentException("Itemstack is not equal as deduped one");
									AllTheLeaks.LOGGER.error("Error checking stack integrity", error);
								}
							}
						}
					}
					if (!IngredientDedupe.MODERNFIX_DEDUPLICATION && !((Lockable) (Object) dedupedIngredient).isLocked()) {
						Streams.of(dedupedIngredient.getItems())
							.peek(stack -> {
								if (stack.isEmpty()) {
									AllTheLeaks.LOGGER.error("ItemStack from Ingredient {} is empty!", List.of(dedupedIngredient.getValues()));
								}
							})
							.forEach(stack -> ((Lockable) (Object) stack).setLocked(true));
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
