package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.uncandango.alltheleaks.mixin.InnerLockable;
import dev.uncandango.alltheleaks.mixin.Lockable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Ingredient.class)
public class IngredientDebugMixin implements InnerLockable {
	@Unique
	private boolean atl$locked = false;

	@Unique
	private boolean atl$innerLocked = false;

	@Override
	public boolean isInnerLocked() {
		return atl$innerLocked;
	}

	@Override
	public void setInnerLocked(boolean locked) {
		this.atl$innerLocked = locked;
	}

	@Override
	public boolean isLocked() {
		return atl$locked;
	}

	@Override
	public void setLocked(boolean locked) {
		this.atl$locked = locked;
	}

	@WrapMethod(method = "getItems")
	private ItemStack[] lockIfNeeded(Operation<ItemStack[]> original){
		var originalArray = original.call();
		if (this.atl$locked && !this.atl$innerLocked){
			this.atl$innerLocked = true;
			for (var stack : originalArray) {
				((Lockable)(Object)stack).setLocked(true);
			}
		}
		return originalArray;
	}
}
