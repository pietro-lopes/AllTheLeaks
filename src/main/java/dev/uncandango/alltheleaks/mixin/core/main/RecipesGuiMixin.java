package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.UpdateablePlayer;
import mezz.jei.gui.recipes.RecipeTransferButton;
import mezz.jei.gui.recipes.RecipesGui;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = RecipesGui.class, remap = false)
public abstract class RecipesGuiMixin extends Screen implements UpdateablePlayer<RecipesGui> {

	@SuppressWarnings("MixinAnnotationTarget")
	@Shadow
	private List<RecipeTransferButton> recipeTransferButtons;

	protected RecipesGuiMixin(Component title) {
		super(title);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void registerInstance(CallbackInfo ci) {
		UpdateablePlayer.register(this);
	}

	@Override
	public void atl$onClientPlayerUpdated(LocalPlayer player) {
		for (RecipeTransferButton button : this.recipeTransferButtons) {
			this.removeWidget((GuiEventListener) button);
		}
		this.recipeTransferButtons.clear();
	}
}
