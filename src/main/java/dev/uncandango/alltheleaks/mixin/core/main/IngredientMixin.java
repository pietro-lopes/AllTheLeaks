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

@Mixin(Ingredient.class)
public abstract class IngredientMixin {

	@ModifyReturnValue(method = "fromValues", at = @At("RETURN"))
	private static Ingredient atl$internFromValues(Ingredient original) {
		if (!original.isEmpty() && original.isVanilla()) {
			return IngredientDedupe.intern(original);
		}
		return original;
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
			return other instanceof TagValueAccessor ingredient$tagvalue && other.getClass() == this.getClass() && ingredient$tagvalue.getTag().location().equals(this.tag.location());
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(tag);
		}
	}
}
