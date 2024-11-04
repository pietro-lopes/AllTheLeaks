package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import jeresources.api.conditionals.LightLevel;
import jeresources.compatibility.minecraft.ExperienceRange;
import jeresources.entry.MobEntry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Supplier;

@Mixin(value = MobEntry.class, remap = false)
public class MobEntryMixin implements UpdateableLevel<MobEntry> {
	@Shadow
	@javax.annotation.Nullable
	private LivingEntity entity;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void registerInstance(Supplier<?> entitySupplier, LightLevel lightLevel, ExperienceRange experience, List<?> biomes, List<?> drops, CallbackInfo ci) {
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		this.entity = null;
	}
}
