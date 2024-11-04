package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.UpdateablePlayer;
import mezz.jei.gui.recipes.layouts.LazySortedRecipeLayoutList;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;

@Mixin(value = LazySortedRecipeLayoutList.class, remap = false)
public class LazySortedRecipeLayoutListMixin implements UpdateablePlayer<LazySortedRecipeLayoutList> {
	@Shadow
	@Mutable
	@Final
	private @Nullable Player player;

	@Shadow
	@Mutable
	@Final
	private @Nullable AbstractContainerMenu container;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void registerInstance(Set<?> recipeSorterStages, AbstractContainerMenu container, Player player, List unsortedList, CallbackInfo ci) {
		UpdateablePlayer.register(this);
	}

	@Override
	public void atl$onClientPlayerUpdated(LocalPlayer player) {
		this.player = player;
		this.container = player.containerMenu;
	}
}
