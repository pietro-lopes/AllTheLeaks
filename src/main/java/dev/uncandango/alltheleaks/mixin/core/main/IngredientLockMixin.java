package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.uncandango.alltheleaks.mixin.InnerLockable;
import dev.uncandango.alltheleaks.mixin.Lockable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Ingredient.class, priority = 600)
public class IngredientLockMixin implements InnerLockable {
	@Shadow
	@Final
	private Ingredient.Value[] values;
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

	@ModifyReturnValue(method = "getItems", at = @At("RETURN"))
	private ItemStack[] atl$lockItemStacks(ItemStack[] original){
		if (this.atl$locked && !atl$innerLocked){
			for (var stack : original) {
				((Lockable)(Object)stack).atl$setLocked(true);
			}
			atl$innerLocked = true;
		}
		return original;
	}

	@Inject(method = "invalidate", at = @At("RETURN"), remap = false)
	private void atl$refreshLock(CallbackInfo ci){
		atl$innerLocked = false;
	}

	@Override
	public void atl$setLocked(boolean locked) {
		this.atl$locked = locked;
	}

}