package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.exceptions.ATLUnsupportedOperation;
import dev.uncandango.alltheleaks.mixin.Lockable;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Locale;

@Mixin(ItemStack.class)
public class ItemStackLockMixin implements Lockable {
	@Unique
	private boolean atl$locked = false;

	@WrapMethod(method = "setCount")
	private void safeSetCount(int count, Operation<Void> original){
		if (!atl$isLocked()) {
			original.call(count);
		} else {
			var error = new ATLUnsupportedOperation("An Ingredient that contains this itemstack was cached and locked by mod AllTheLeaks, modifications of ItemStacks in Ingredients are not allowed!");
			var message = String.format(Locale.ROOT, "Cannot set count with value \"%s\" to itemstack \"%s\".", count, this);
			AllTheLeaks.LOGGER.warn(message);
			throw error;
		}
	}

	@WrapMethod(method = "set")
	private Object safeSetComponent(DataComponentType component, Object value, Operation<Object> original){
		if (!atl$isLocked()) {
			return original.call(component, value);
		} else {
			var error = new ATLUnsupportedOperation("An Ingredient that contains this itemstack was cached and locked by mod AllTheLeaks, modifications of ItemStacks in Ingredients are not allowed!");
			var message = String.format(Locale.ROOT, "Cannot set component \"%s\" with value \"%s\" to itemstack \"%s\".", component, value, this);
			AllTheLeaks.LOGGER.warn(message);
			throw error;
		}
	}

	@WrapMethod(method = "remove")
	private Object safeRemoveComponent(DataComponentType component, Operation<Object> original){
		if (!atl$isLocked()) {
			return original.call(component);
		} else {
			var error = new ATLUnsupportedOperation("An Ingredient that contains this itemstack was cached and locked by mod AllTheLeaks, modifications of ItemStacks in Ingredients are not allowed!");
			var value = ((ItemStack)(Object)this).get(component);
			var message = String.format(Locale.ROOT, "Cannot remove component \"%s\" with value \"%s\" from itemstack \"%s\".", component, value, this);
			AllTheLeaks.LOGGER.warn(message);
			throw error;
		}
	}

	@WrapMethod(method = "applyComponentsAndValidate")
	private void safeApplyAndValidateComponent(DataComponentPatch components, Operation<Void> original){
		if (!atl$isLocked()) {
			original.call(components);
		} else {
			var error = new ATLUnsupportedOperation("An Ingredient that contains this itemstack was cached and locked by mod AllTheLeaks, modifications of ItemStacks in Ingredients are not allowed!");
			var message = String.format(Locale.ROOT, "Cannot apply and validate component patch \"%s\" to itemstack \"%s\".", components, this);
			AllTheLeaks.LOGGER.warn(message);
			throw error;
		}
	}

	@WrapMethod(method = "applyComponents(Lnet/minecraft/core/component/DataComponentMap;)V")
	private void safeApplyComponentMap(DataComponentMap components, Operation<Void> original){
		if (!atl$isLocked()) {
			original.call(components);
		} else {
			var error = new ATLUnsupportedOperation("An Ingredient that contains this itemstack was cached and locked by mod AllTheLeaks, modifications of ItemStacks in Ingredients are not allowed!");
			var message = String.format(Locale.ROOT, "Cannot apply component map \"%s\" to itemstack \"%s\".", components, this);
			AllTheLeaks.LOGGER.warn(message);
			throw error;
		}
	}

	@WrapMethod(method = "applyComponents(Lnet/minecraft/core/component/DataComponentPatch;)V")
	private void safeApplyComponentPatch(DataComponentPatch components, Operation<Void> original){
		if (!atl$isLocked()) {
			original.call(components);
		} else {
			var error = new ATLUnsupportedOperation("An Ingredient that contains this itemstack was cached and locked by mod AllTheLeaks, modifications of ItemStacks in Ingredients are not allowed!");
			var message = String.format(Locale.ROOT, "Cannot apply component patch \"%s\" to itemstack \"%s\".", components, this);
			AllTheLeaks.LOGGER.warn(message);
			throw error;
		}
	}

	@Override
	public boolean atl$isLocked() {
		return atl$locked;
	}

	@Override
	public void atl$setLocked(boolean locked) {
		atl$locked = locked;
	}
}
