package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.item.ItemEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "mekanism.client.render.transmitter.RenderLogisticalTransporter$LazyItemRenderer")
public class LazyItemRendererMixin implements UpdateableLevel {
	@Shadow
	private @Nullable ItemEntity entityItem;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void grabInstance(CallbackInfo ci){
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		if (this.entityItem != null && this.entityItem.level() != level) {
			this.entityItem = null;
		}
	}
}
