package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.exceptions.ATLUnsupportedOperation;
import dev.uncandango.alltheleaks.mixin.Lockable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.Locale;

@Mixin(ItemStack.class)
public abstract class ItemStackLockMixin extends CapabilityProvider<ItemStack> implements Lockable {
	@Shadow
	@Nullable
	private CompoundTag tag;

	protected ItemStackLockMixin(Class<ItemStack> baseClass) {
		super(baseClass);
	}

	@Shadow
	public abstract Item getItem();

	@Unique
	private boolean atl$locked = false;

	@WrapMethod(method = "setCount")
	private void atl$safeSetCount(int count, Operation<Void> original){
		if (!atl$isLocked()) {
			original.call(count);
		} else {
			var error = new ATLUnsupportedOperation("An Ingredient that contains this itemstack was cached and locked by mod AllTheLeaks, modifications of ItemStacks in Ingredients are not allowed!");
			var message = String.format(Locale.ROOT, "Cannot set count with value \"%s\" to itemstack \"%s\".", count, this);
			AllTheLeaks.LOGGER.warn(message);
			throw error;
		}
	}

	@WrapMethod(method = "setTag")
	private void atl$safeSetTag(CompoundTag compoundTag, Operation<Void> original){
		if (!atl$isLocked()) {
			original.call(compoundTag);
		} else {
			var error = new ATLUnsupportedOperation("An Ingredient that contains this itemstack was cached and locked by mod AllTheLeaks, modifications of ItemStacks in Ingredients are not allowed!");
			var message = String.format(Locale.ROOT, "Cannot set tags on items locked at creation: itemstack \"%s\".",this);
			AllTheLeaks.LOGGER.warn(message);
			throw error;
		}
	}

	@WrapMethod(method = "setDamageValue")
	private void atl$safeSetDamageValue(int damage, Operation<Void> original){
		if (!atl$isLocked()) {
			original.call(damage);
		} else {
			var error = new ATLUnsupportedOperation("An Ingredient that contains this itemstack was cached and locked by mod AllTheLeaks, modifications of ItemStacks in Ingredients are not allowed!");
			var message = String.format(Locale.ROOT, "Cannot set damage values on locked itemstack \"%s\".",this);
			AllTheLeaks.LOGGER.warn(message);
			throw error;
		}
	}

	@WrapMethod(method = "removeTagKey")
	private void atl$safeRemoveTagKey(String key, Operation<Void> original){
		if (!atl$isLocked()) {
			original.call(key);
		} else {
			var error = new ATLUnsupportedOperation("An Ingredient that contains this itemstack was cached and locked by mod AllTheLeaks, modifications of ItemStacks in Ingredients are not allowed!");
			var message = String.format(Locale.ROOT, "Cannot remove keys from tags on items locked at creation: itemstack \"%s\".",this);
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
		if (atl$locked && locked) return;
		this.getCapabilities();
		if (this.tag != null) {
			((Lockable)this.tag).atl$setLocked(true);
		}
		atl$locked = locked;
	}


}
