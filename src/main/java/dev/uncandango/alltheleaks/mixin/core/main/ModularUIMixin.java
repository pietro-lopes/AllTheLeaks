package dev.uncandango.alltheleaks.mixin.core.main;

import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.uncandango.alltheleaks.mixin.UpdateablePlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModularUI.class, remap = false)
public abstract class ModularUIMixin implements UpdateablePlayer<ModularUI> {
	@Shadow
	@Mutable
	@Final
	public Player entityPlayer;

	@Inject(method = "<init>(Lcom/lowdragmc/lowdraglib/gui/widget/WidgetGroup;Lcom/lowdragmc/lowdraglib/gui/modular/IUIHolder;Lnet/minecraft/world/entity/player/Player;)V", at = @At("TAIL"))
	private void registerInstance(WidgetGroup mainGroup, IUIHolder holder, Player entityPlayer, CallbackInfo ci) {
		UpdateablePlayer.register(this);
	}

	@Override
	public void atl$onClientPlayerUpdated(LocalPlayer player) {
		this.entityPlayer = player;
	}
}
