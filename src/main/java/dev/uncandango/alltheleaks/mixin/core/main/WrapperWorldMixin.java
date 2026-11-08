package dev.uncandango.alltheleaks.mixin.core.main;

import mcinterface1201.WrapperWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WrapperWorld.class)
public class WrapperWorldMixin {
	@Inject(method = "onIVWorldUnload", at = @At(value = "INVOKE", target = "Lmcinterface1201/WrapperWorld;onUnload()V"), remap = false)
	private void atl$unregisterObject(LevelEvent.Unload event, CallbackInfo ci){
		MinecraftForge.EVENT_BUS.unregister(this);
	}
}
