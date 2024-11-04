package dev.uncandango.alltheleaks.mixin.core.main;

import com.google.common.base.Objects;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.IngredientDedupe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;

@Mixin(Ingredient.class)
public abstract class IngredientMixin {

	@Shadow
	@Final
	private Ingredient.Value[] values;

	@ModifyReturnValue(method = "fromValues", at = @At("RETURN"))
	private static Ingredient internFromValues(Ingredient original) {
		if (!original.isEmpty()) {
			return IngredientDedupe.intern(original);
		}
		return original;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Ingredient ingredient) {
			if (ingredient.isVanilla() && this.isVanilla() && Arrays.equals((Object[]) this.values, (Object[]) ((IngredientAccessor) other).getValues())) {
				return true;
			}
		}
		return false;
	}

	@Shadow(remap = false)
	public abstract boolean isVanilla();

	@Override
	public int hashCode() {
		if (!this.isVanilla()) {
			return super.hashCode();
		}
		return Arrays.hashCode(this.values);
	}

	@Mixin(Ingredient.class)
	public interface IngredientAccessor {
		@Accessor("values")
		Ingredient.Value[] getValues();
	}

	@Mixin(Ingredient.TagValue.class)
	public interface TagValueAccessor {
		@Accessor("tag")
		TagKey<Item> getTag();
	}

	@Mixin(Ingredient.ItemValue.class)
	public interface ItemValueAccessor {
		@Accessor("item")
		ItemStack getItem();
	}

	@Mixin(Ingredient.TagValue.class)
	public static class TagValueMixin {
		@Shadow
		@Final
		private TagKey<Item> tag;

		@Override
		public boolean equals(Object other) {
			return other instanceof TagValueAccessor ingredient$tagvalue && other.getClass() == Ingredient.TagValue.class && this.getClass().equals(Ingredient.TagValue.class) && ingredient$tagvalue.getTag().location().equals(this.tag.location());
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(tag);
		}
	}

	@Mixin(Ingredient.ItemValue.class)
	public static class ItemValueMixin {
		@Shadow
		@Final
		private ItemStack item;

		@Override
		public boolean equals(Object other) {
			return other instanceof ItemValueAccessor ingredient$itemvalue && other.getClass() == Ingredient.ItemValue.class && this.getClass().equals(Ingredient.ItemValue.class) && ingredient$itemvalue.getItem().getItem().equals(this.item.getItem()) && ingredient$itemvalue.getItem().getCount() == this.item.getCount();
		}

		@Override
		public int hashCode() {
			return 31 * this.item.getItem().hashCode() + this.item.getCount();
		}
	}
}
