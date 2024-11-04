package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.UpdateablePlayer;
import mezz.jei.gui.recipes.RecipeTransferButton;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RecipeTransferButton.class, remap = false)
public abstract class RecipeTransferButtonMixin implements UpdateablePlayer<RecipeTransferButton> {

	@Shadow
	public abstract void update(@Nullable AbstractContainerMenu parentContainer, @Nullable Player player);

	@Inject(method = "<init>", at = @At("TAIL"))
	private void registerInstance(CallbackInfo ci){
		UpdateablePlayer.register(this);
	}

	@Override
	public void atl$onClientPlayerUpdated(LocalPlayer player) {
		this.update(player.containerMenu, player);
	}
}
