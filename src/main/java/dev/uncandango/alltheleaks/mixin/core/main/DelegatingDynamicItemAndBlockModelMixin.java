package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.client.model.DelegatingDynamicItemAndBlockModel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DelegatingDynamicItemAndBlockModel.class, remap = false)
public class DelegatingDynamicItemAndBlockModelMixin implements UpdateableLevel<DelegatingDynamicItemAndBlockModel> {
	@Shadow
	@Mutable
	@Final
	protected Level world;

	@Inject(method = "<init>(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At("RETURN"))
	private void registerInstance(ItemStack itemStack, Level world, LivingEntity entity, CallbackInfo ci) {
		if (world instanceof ClientLevel) {
			UpdateableLevel.register(this);
		}
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		if (level != null && this.world != level) {
			this.world = level;
		}
	}
}
