package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.uncandango.alltheleaks.metrics.client.mods.jei.ItemStackCreationStatistics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {

	@WrapMethod(method = "setEntityRepresentation")
	private void atl$checkEmpty(Entity entity, Operation<Void> original) {
		if (!((ItemStack) (Object) this).isEmpty()) {
			original.call(entity);
		}
	}

	@Mixin(ItemStack.class)
	public static class Statistics {
		@Inject(method = {"<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/nbt/CompoundTag;)V","<init>(Lnet/minecraft/nbt/CompoundTag;)V"}, at = @At("TAIL"))
		private void increaseStackCount(CallbackInfo ci) {
			if (ItemStackCreationStatistics.currentPlugin != null){
				ItemStackCreationStatistics.addToCounter();
			}
		}
	}
}
