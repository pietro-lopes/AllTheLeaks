package dev.uncandango.alltheleaks.feature.common.mods.minecraft;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.Lockable;
import dev.uncandango.alltheleaks.mixin.core.main.IngredientMixin;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fml.loading.LoadingModList;
import org.embeddedt.modernfix.core.ModernFixMixinPlugin;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Issue(modId = "minecraft", issueId = "Ingredient Deduplication" ,versionRange = "1.20.1", mixins = {"main.IngredientMixin", "main.IngredientMixin$IngredientAccessor", "main.IngredientMixin$TagValueMixin", "main.IngredientMixin$TagValueAccessor", "main.IngredientMixin$ItemValueAccessor", "main.IngredientLockMixin", "main.ItemStackLockMixin", "main.CompoundLockMixin", "main.ListTagLockMixin", "main.IntArrayTagMixin", "main.LongArrayTagMixin", "main.ByteArrayTagMixin","main.IngredientItemValueMixin", "main.CapabilityProviderAccessor"}, config = "ingredientDedupe", configActivated = false,
description = "Deduplicates VANILLA ingredients to reduce memory usage")
public class IngredientDedupe {
	private static final ObjectOpenCustomHashSet<Ingredient> INGREDIENT_CACHE;
	public static final boolean MODERNFIX_DEDUPLICATION;

	static {
		var BASIC_HASH_STRATEGY = new Hash.Strategy<Ingredient>() {
			@Override
			public int hashCode(Ingredient o) {
				if (o.isVanilla() && o instanceof IngredientMixin.IngredientAccessor accessor) {
					if (o == null)
						return 0;

					int result = 1;

					for (Object element : accessor.getValues())
						if (element instanceof IngredientMixin.ItemValueAccessor iv) {
							result = 31 * result + ItemStackLinkedSet.TYPE_AND_TAG.hashCode(iv.getItem()) + iv.getItem().getCount();
						} else {
							result = 31 * result + (element == null ? 0 : element.hashCode());
						}
					return result;
				}
				return Objects.hashCode(o);
			}

			@Override
			public boolean equals(Ingredient a, Ingredient b) {
				if (b == null) return false;
				var aValues = ((IngredientMixin.IngredientAccessor) a).getValues();
				var bValues = ((IngredientMixin.IngredientAccessor) b).getValues();
				if (aValues.length != bValues.length) return false;
				for (int i = 0; i < aValues.length; i++) {
					var aValue = aValues[i];
					var bValue = bValues[i];
					if (aValue.getClass() != bValue.getClass()) return false;
					if (aValue.getClass() == Ingredient.TagValue.class) {
						if (!aValue.equals(bValue)) {
							return false;
						}
					} else {
						if (aValue.getClass() == Ingredient.ItemValue.class) {
							if (MODERNFIX_DEDUPLICATION) {
								if (aValue != bValue) {
									return false;
								}
							} else {
								var aItem = ((IngredientMixin.ItemValueAccessor) aValue).getItem();
								var bItem = ((IngredientMixin.ItemValueAccessor) bValue).getItem();
								if (!ItemStack.isSameItemSameTags(aItem, bItem) || aItem.getCount() != bItem.getCount()){
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

	public synchronized static Ingredient intern(Ingredient ingredient) {
		var deduped = INGREDIENT_CACHE.addOrGet(ingredient);
		if (!((Lockable)deduped).atl$isLocked()) {
			((Lockable)deduped).atl$setLocked(true);
		}
		return deduped;
	}
}
