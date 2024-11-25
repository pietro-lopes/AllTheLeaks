package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SynchedEntityData.class)
public class SynchedEntityDataMixin<T> {
	@WrapOperation(method = "getNonDefaultValues", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData$DataItem;value()Lnet/minecraft/network/syncher/SynchedEntityData$DataValue;"))
	private SynchedEntityData.DataValue<T> atl$addToTrackedItemStack(SynchedEntityData.DataItem<T> instance, Operation<SynchedEntityData.DataValue<T>> original){
		var result = original.call(instance);
		if (result.value() instanceof ItemStack stack) {
			UpdateableLevel.register((UpdateableLevel<?>) (Object) stack);
		}
		return result;
	}
}
