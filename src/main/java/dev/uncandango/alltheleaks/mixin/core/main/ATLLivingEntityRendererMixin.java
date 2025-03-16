package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.leaks.client.mods.tombstone.UntrackedIssue001;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.mixin.UpdateablePlayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class ATLLivingEntityRendererMixin implements UpdateablePlayer<LivingEntityRenderer<?,?>>, UpdateableLevel<LivingEntityRenderer<?,?>> {
	@Inject(method = "<init>", at = @At("TAIL"))
	private void registerInstance(EntityRendererProvider.Context context, EntityModel<?> model, float shadowRadius, CallbackInfo ci) {
		UpdateablePlayer.register(this);
		UpdateableLevel.register(this);
	}

	private static boolean atl$tombstoneEnabled = false;
	private static boolean atl$emfEnabled = false;
	private static boolean atl$etfEnabled = false;

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		if (atl$tombstoneEnabled) UntrackedIssue001.clearLivingEntityFromRenderer((LivingEntityRenderer<?,?>) (Object)this);
		if (atl$emfEnabled) dev.uncandango.alltheleaks.leaks.client.mods.entity_model_features.UntrackedIssue001.clearCachedEntityFromRenderer((LivingEntityRenderer<?,?>) (Object)this, level);
		if (atl$etfEnabled) dev.uncandango.alltheleaks.leaks.client.mods.entity_texture_features.UntrackedIssue001.clearCachedEntityFromRenderer((LivingEntityRenderer<?,?>) (Object)this, level);
	}

	@Override
	public void atl$onClientPlayerUpdated(LocalPlayer player) {
		if (atl$tombstoneEnabled) UntrackedIssue001.clearLivingEntityFromRenderer((LivingEntityRenderer<?,?>) (Object)this);
	}
}
