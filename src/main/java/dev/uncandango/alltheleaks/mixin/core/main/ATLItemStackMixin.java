package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.uncandango.alltheleaks.metrics.client.mods.jei.ItemStackCreationStatistics;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ATLItemStackMixin implements UpdateableLevel<ItemStack> {

	@Shadow
	@javax.annotation.Nullable
	private Entity entityRepresentation;

	@Shadow
	public abstract void setEntityRepresentation(@Nullable Entity entity);

	@WrapMethod(method = "setEntityRepresentation")
	private void atl$checkEmpty(Entity entity, Operation<Void> original) {
		if (!((ItemStack) (Object) this).isEmpty()) {
			original.call(entity);
		}
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		if (this.entityRepresentation != null && this.entityRepresentation.level() != level) {
			this.setEntityRepresentation(null);
		}
	}

	@Mixin(ItemStack.class)
	public static class Statistics {
		@Inject(method = {"<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/nbt/CompoundTag;)V", "<init>(Lnet/minecraft/nbt/CompoundTag;)V"}, at = @At("TAIL"))
		private void atl$increaseStackCount(CallbackInfo ci) {
			if (ItemStackCreationStatistics.currentPlugin != null) {
				ItemStackCreationStatistics.addToCounter();
			}
		}
	}
}
