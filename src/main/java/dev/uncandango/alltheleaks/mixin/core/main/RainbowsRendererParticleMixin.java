package dev.uncandango.alltheleaks.mixin.core.main;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rainbows.util.RainbowsRendererParticle;

@Mixin(RainbowsRendererParticle.class)
public abstract class RainbowsRendererParticleMixin extends TextureSheetParticle {

	@Shadow
	@Final
	public Player player;

	protected RainbowsRendererParticleMixin(ClientLevel level, double x, double y, double z) {
		super(level, x, y, z);
	}

	@Inject(method = "tick", at=@At("HEAD"))
	private void checkIfSamePlayer(CallbackInfo ci){
		if (this.player.isRemoved()){
			this.remove();
		}
	}
}
