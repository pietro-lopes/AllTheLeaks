package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.leaks.common.mods.mts.UntrackedIssue002;
import mcinterface1201.ABuilderEntityBase;
import mcinterface1201.BuilderEntityRenderForwarder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuilderEntityRenderForwarder.class)
public class BuilderEntityRenderForwarderMixin extends ABuilderEntityBase {
	@Shadow
	protected Player playerFollowing;

	public BuilderEntityRenderForwarderMixin(Level level) {
		super(level);
	}

	@Inject(method = "baseTick", at = @At("TAIL"))
	private void atl$clearFollowingEntity(CallbackInfo ci) {
		if (UntrackedIssue002.isEntityInvalid(this)) {
			this.playerFollowing = null;
		}
	}
}
