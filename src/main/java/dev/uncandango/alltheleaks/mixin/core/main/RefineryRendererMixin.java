package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rearth.oritech.client.renderers.RefineryRenderer;

import java.util.Map;

@Mixin(RefineryRenderer.class)
public class RefineryRendererMixin<T> implements UpdateableLevel<RefineryRenderer<?>> {
	@Shadow
	@Final
	private Map<T, Object> tankHeights;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void grabInstance(String model, CallbackInfo ci){
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		this.tankHeights.clear();
	}
}
