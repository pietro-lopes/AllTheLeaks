package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import jeresources.entry.AbstractVillagerEntry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.npc.AbstractVillager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractVillagerEntry.class, remap = false)
public abstract class AbstractVillagerEntryMixin<T extends AbstractVillager> implements UpdateableLevel<AbstractVillagerEntry<?>> {
	@Shadow
	protected T entity;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void registerInstance(CallbackInfo ci) {
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		this.entity = null;
	}
}
