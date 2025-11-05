package dev.uncandango.alltheleaks.mixin.core.debug;

import dev.uncandango.alltheleaks.AllTheLeaks;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
	@Unique
	private boolean atl$tickingBlockEntities;

	@Unique
	private boolean atl$previousTickingBlockEntities;

	@Inject(method = "tick", at = @At(value = "HEAD"))
	private void clearValue(BooleanSupplier hasTimeLeft, CallbackInfo ci){
		atl$tickingBlockEntities = false;
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;tickBlockEntities()V"))
	private void updateTicking(BooleanSupplier hasTimeLeft, CallbackInfo ci){
		atl$tickingBlockEntities = true;
	}

	@Inject(method = "tick", at = @At(value = "TAIL"))
	private void logDiff(BooleanSupplier hasTimeLeft, CallbackInfo ci){
		if (atl$tickingBlockEntities != atl$previousTickingBlockEntities) {
			AllTheLeaks.LOGGER.info("Ticking block entities at {} is now {}", ((ServerLevel)(Object)this).dimension().location(), atl$tickingBlockEntities);
		}
		atl$previousTickingBlockEntities = atl$tickingBlockEntities;
	}
}
