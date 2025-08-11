package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.exceptions.ATLUnsupportedOperation;
import dev.uncandango.alltheleaks.mixin.Lockable;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Locale;

@Mixin(ByteArrayTag.class)
public class ByteArrayTagMixin implements Lockable {
	@Unique
	private boolean atl$locked = false;

	/*
	   public abstract T set(int p_128318_, T p_128319_);

   public abstract void add(int p_128315_, T p_128316_);

   public abstract T remove(int p_128313_);

   public abstract boolean setTag(int index, Tag tag);

   public abstract boolean addTag(int index, Tag tag);
	 */

	@WrapMethod(method = {"set(ILnet/minecraft/nbt/ByteTag;)Lnet/minecraft/nbt/ByteTag;"})
	private ByteTag alt$checkIfLockedSet(int p_128196_, ByteTag p_128197_, Operation<ByteTag> original){
		if (atl$locked) {
			var error = new ATLUnsupportedOperation("An ByteArrayTag was locked by mod AllTheLeaks, modifications of ByteArrayTag from locked CompoundTags are not allowed!");
			var message = String.format(Locale.ROOT, "Cannot set entries on : bytearraytag \"%s\".",this);
			AllTheLeaks.LOGGER.warn(message);
			throw error;
		}
		return original.call(p_128196_, p_128197_);
	}

	@WrapMethod(method = {"add(ILnet/minecraft/nbt/ByteTag;)V"})
	private void alt$checkIfLockedAdd(int p_128215_, ByteTag p_128216_, Operation<Void> original){
		if (atl$locked) {
			var error = new ATLUnsupportedOperation("An ByteArrayTag was locked by mod AllTheLeaks, modifications of ByteArrayTag from locked CompoundTags are not allowed!");
			var message = String.format(Locale.ROOT, "Cannot add entries on : bytearraytag \"%s\".",this);
			AllTheLeaks.LOGGER.warn(message);
			throw error;
		}
		original.call(p_128215_, p_128216_);
	}

	@WrapMethod(method = {"remove(I)Lnet/minecraft/nbt/ByteTag;"})
	private ByteTag alt$checkIfLockedRemove(int p_128627_, Operation<ByteTag> original){
		if (atl$locked) {
			var error = new ATLUnsupportedOperation("An ByteArrayTag was locked by mod AllTheLeaks, modifications of ByteArrayTag from locked CompoundTags are not allowed!");
			var message = String.format(Locale.ROOT, "Cannot remove entries on : bytearraytag \"%s\".",this);
			AllTheLeaks.LOGGER.warn(message);
			throw error;
		}
		return original.call(p_128627_);
	}

	@WrapMethod(method = {"addTag"})
	private boolean alt$checkIfLockedAddTag(int index, Tag nbt, Operation<Boolean> original){
		if (atl$locked) {
			var error = new ATLUnsupportedOperation("An ByteArrayTag was locked by mod AllTheLeaks, modifications of ByteArrayTag from locked CompoundTags are not allowed!");
			var message = String.format(Locale.ROOT, "Cannot add entries on : bytearraytag \"%s\".",this);
			AllTheLeaks.LOGGER.warn(message);
			throw error;
		}
		return original.call(index, nbt);
	}

	@WrapMethod(method = {"setTag"})
	private boolean alt$checkIfLockedSetTag(int index, Tag nbt, Operation<Boolean> original){
		if (atl$locked) {
			var error = new ATLUnsupportedOperation("An LongArrayTag was locked by mod AllTheLeaks, modifications of LongArrayTag from locked CompoundTags are not allowed!");
			var message = String.format(Locale.ROOT, "Cannot set entries on : longarraytag \"%s\".",this);
			AllTheLeaks.LOGGER.warn(message);
			throw error;
		}
		return original.call(index, nbt);
	}

	@WrapMethod(method = {"clear"})
	private void alt$checkIfLockedClear(Operation<Void> original){
		if (atl$locked) {
			var error = new ATLUnsupportedOperation("An LongArrayTag was locked by mod AllTheLeaks, modifications of LongArrayTag from locked CompoundTags are not allowed!");
			var message = String.format(Locale.ROOT, "Cannot clear entries on : longarraytag \"%s\".",this);
			AllTheLeaks.LOGGER.warn(message);
			throw error;
		}
		original.call();
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
