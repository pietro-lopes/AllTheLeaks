package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.uncandango.alltheleaks.mixin.core.main.accessor.HolderReferenceAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

// Unused, not safe to do it, many mods relies on this "bug"
@Mixin(Holder.Reference.class)
public abstract class HolderReferenceMixin<T> implements Holder<T> {
	@Shadow
	@Final
	private HolderOwner<T> owner;

	@ModifyReturnValue(method = "equals", at = @At(value = "RETURN", ordinal = 1))
	private boolean checkSameOwner(boolean original, Object obj){
		if (original && obj instanceof HolderReferenceAccessor<?> accessor) {
			return this.owner == accessor.atl$getOwner();
		}
		return original;
	}
}
