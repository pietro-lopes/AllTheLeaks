package dev.uncandango.alltheleaks.mixin.core.main;

import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import blusunrize.immersiveengineering.client.manual.ManualElementMultiblock;
import blusunrize.lib.manual.ManualInstance;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ManualElementMultiblock.class)
public class ManualElementMultiblockMixin implements UpdateableLevel<ManualElementMultiblock> {
	@Shadow
	@Final
	@Mutable
	private ClientLevel level;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void registerInstance(ManualInstance manual, MultiblockHandler.IMultiblock multiblock, CallbackInfo ci){
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		if (level == null) return;
		this.level = level;
	}
}
