package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import dev.uncandango.alltheleaks.mixin.Lockable;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(targets = {"mezz.jei.library.ingredients.itemStacks.NormalizedTypedItemStack"}, remap = false)
public class NormalizedTypedItemStackMixin {

	@CompatibleHashes(values = {1007239182})
	@ModifyArg(method = "create", at = @At(value = "INVOKE", target = "Lmezz/jei/library/ingredients/itemStacks/NormalizedTypedItemStack;<init>(Lnet/minecraft/core/Holder;Lnet/minecraft/nbt/CompoundTag;)V"), index = 1)
	private static CompoundTag atl$safeTag(CompoundTag par2){
		CompoundTag arg = par2;
		if (par2 instanceof Lockable lockable && lockable.atl$isLocked()) {
			arg = par2.copy();
		}
		return arg;
	}
}
