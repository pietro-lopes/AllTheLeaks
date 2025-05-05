package dev.uncandango.alltheleaks.mixin.core.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.bus.EventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MinecraftMixin2 {
	@Shadow
	@Nullable
	public ClientLevel level;

	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;level:Lnet/minecraft/client/multiplayer/ClientLevel;"), method = "clearClientLevel")
	private void atl$fireLevelUnload(Screen nextScreen, CallbackInfo ci){
		if (this.level != null) NeoForge.EVENT_BUS.post(new LevelEvent.Unload(this.level));
	}
}
