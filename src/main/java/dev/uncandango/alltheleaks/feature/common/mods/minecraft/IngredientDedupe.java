package dev.uncandango.alltheleaks.feature.common.mods.minecraft;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.PatchedDataComponentMapAccessor;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.fml.loading.LoadingModList;
import org.embeddedt.modernfix.core.ModernFixMixinPlugin;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Issue(modId = "minecraft", issueId = "Ingredient Deduplication",versionRange = "1.21.1", mixins = {"main.IngredientMixin", "main.IngredientMixin$IngredientAccessor", "accessor.PatchedDataComponentMapAccessor", "accessor.HolderReferenceAccessor", "main.HolderReferenceMixin", "main.IngredientLockMixin", "main.ItemStackLockMixin"}, config = "ingredientDedupe", configActivated = false)
public class IngredientDedupe {
	private static final ObjectOpenCustomHashSet<Ingredient> INGREDIENT_CACHE;
	private static final boolean MODERNFIX_DEDUPLICATION;

	static {
		var BASIC_HASH_STRATEGY = new Hash.Strategy<Ingredient>() {
			@Override
			public int hashCode(Ingredient o) {
				if (o == null)
					return 0;

				int result = 1;

				for (Object element : o.getValues())
					if (element instanceof Ingredient.ItemValue iv) {
						result = 31 * result + ItemStackLinkedSet.TYPE_AND_TAG.hashCode(iv.item()) + (iv.item().getCount() > 1 ? iv.item().getCount() : 0);
					} else {
						result = 31 * result + (element == null ? 0 : element.hashCode());
					}
				return result;
			}

			@Override
			public boolean equals(Ingredient a, Ingredient b) {
				if (b == null) return false;
				var aValues = a.getValues();
				var bValues = b.getValues();
				if (aValues.length != bValues.length) return false;
				for (int i = 0; i < aValues.length; i++) {
					var aValue = aValues[i];
					var bValue = bValues[i];
					if (aValue.getClass() != bValue.getClass()) return false;
					if (aValue instanceof Ingredient.TagValue && bValue instanceof Ingredient.TagValue) {
						if (!aValue.equals(bValue)) {
							return false;
						}
					} else {
						if (aValue instanceof Ingredient.ItemValue(ItemStack item) && bValue instanceof Ingredient.ItemValue(ItemStack item1)) {
							if (MODERNFIX_DEDUPLICATION) {
								if (aValue != bValue) {
									return false;
								}
							} else {
								// Debug
//								if (ItemStack.isSameItemSameComponents(item, item1) && !strictIsSameItemSameComponents(item, item1)) {
//									AllTheLeaks.LOGGER.warn("Item {} and {} are not the same?", item, item1);
//								}
								if (item.getCount() != item1.getCount() || !ItemStack.isSameItemSameComponents(item, item1)){
									return false;
								}
							}
						} else return false;
					}
				}
				return true;
			}
		};
		INGREDIENT_CACHE = new ObjectOpenCustomHashSet<>(BASIC_HASH_STRATEGY);
		if (LoadingModList.get().getModFileById("modernfix") != null) {
			MODERNFIX_DEDUPLICATION = ModernFixMixinPlugin.instance.isOptionEnabled("perf.ingredient_item_deduplication.IngredientMixin");
		} else MODERNFIX_DEDUPLICATION = false;
	}

	// for debug
	private static boolean strictIsSameItemSameComponents(ItemStack item1, ItemStack item2) {
		if (item1.getComponents() instanceof PatchedDataComponentMapAccessor accessor1 && item2.getComponents() instanceof PatchedDataComponentMapAccessor accessor2) {
			if (!accessor1.atl$getPrototype().equals(accessor2.atl$getPrototype())) {
				return false;
			}
			var aPatch = accessor1.atl$getPatch();
			var bPatch = accessor2.atl$getPatch();
			if (aPatch != bPatch) {
				if (aPatch.size() != bPatch.size()) {
					return false;
				}
				for (var entry : Reference2ObjectMaps.fastIterable(aPatch)) {
					var value = bPatch.get(entry.getKey());
					if (value == null) {
						return false;
					}
					if (value.isPresent() != entry.getValue().isPresent()) {
						return false;
					} else if (value.isPresent() && value.get() != entry.getValue().get()) {
						return false;
					}
				}
			}
			return true;
		} else {
			return item1.getComponents() == item2.getComponents();
		}
	}

	public synchronized static Ingredient intern(Ingredient ingredient) {
		return INGREDIENT_CACHE.addOrGet(ingredient);
	}

}
