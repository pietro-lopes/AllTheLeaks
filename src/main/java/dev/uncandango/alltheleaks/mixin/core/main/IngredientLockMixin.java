package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.uncandango.alltheleaks.mixin.InnerLockable;
import dev.uncandango.alltheleaks.mixin.Lockable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Ingredient.class)
public class IngredientLockMixin implements InnerLockable {
	@Unique
	private boolean atl$locked = false;

	@Unique
	private boolean atl$innerLocked = false;

	@Override
	public boolean atl$isInnerLocked() {
		return atl$innerLocked;
	}

	@Override
	public void atl$setInnerLocked(boolean locked) {
		this.atl$innerLocked = locked;
	}

	@Override
	public boolean atl$isLocked() {
		return atl$locked;
	}

	@Override
	public void atl$setLocked(boolean locked) {
		this.atl$locked = locked;
	}

	@ModifyReturnValue(method = "getItems", at = @At("RETURN"))
	private ItemStack[] lockIfNeeded(ItemStack[] original){
		if (this.atl$locked && !this.atl$innerLocked){
			for (var stack : original) {
				((Lockable)(Object)stack).atl$setLocked(true);
			}
			this.atl$innerLocked = true;
		}
		return original;
	}
}
