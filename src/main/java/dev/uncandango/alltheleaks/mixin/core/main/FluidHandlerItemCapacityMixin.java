package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FluidHandlerItemCapacity.class)
public abstract class FluidHandlerItemCapacityMixin extends FluidHandlerItemStack {

	public FluidHandlerItemCapacityMixin(@NotNull ItemStack container, int capacity) {
		super(container, capacity);
	}

	@CompatibleHashes(values = {823407323})
	@WrapOperation(method = "getCapacity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getOrCreateTag()Lnet/minecraft/nbt/CompoundTag;"))
	private CompoundTag atl$safeTag(ItemStack instance, Operation<CompoundTag> original, @Cancellable CallbackInfoReturnable<Integer> cir){
		if (instance.hasTag()) {
			return original.call(instance);
		}
		cir.setReturnValue(this.capacity);
		return null;
	}
}
