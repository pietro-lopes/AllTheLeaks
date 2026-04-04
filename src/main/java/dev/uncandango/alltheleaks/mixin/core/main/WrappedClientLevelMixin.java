package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.createmod.catnip.levelWrappers.WrappedClientLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WrappedClientLevel.class)
public class WrappedClientLevelMixin implements UpdateableLevel<WrappedClientLevel> {

	@Inject(method = "<init>", at = @At("TAIL"))
	private void grabInstance(CallbackInfo ci) {
		UpdateableLevel.register(this);
	}

	@Shadow
	protected Level level;

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		if (this.level != level) {
			NeoForge.EVENT_BUS.post(new LevelEvent.Unload((WrappedClientLevel)(Object)this));
		}
	}
}
