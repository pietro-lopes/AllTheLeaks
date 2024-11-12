package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.Lockable;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Ingredient.class)
public class IngredientDebugMixin implements Lockable {
	@Unique
	private boolean atl$locked = false;

	@Override
	public boolean isLocked() {
		return atl$locked;
	}

	@Override
	public void setLocked(boolean locked) {
		this.atl$locked = locked;
	}

}
