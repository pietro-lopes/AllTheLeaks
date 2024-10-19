package dev.uncandango.alltheleaks.mixin.core.main;

import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.uncandango.alltheleaks.mixin.ModularUIExtension;
import dev.uncandango.alltheleaks.mods.Gtceu;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;

@Pseudo
@Mixin(value = ModularUI.class, remap = false)
public class ModularUIMixin implements ModularUIExtension {
    @Mutable
    @Shadow @Final public Player entityPlayer;

    @Inject(method = "<init>(Lcom/lowdragmc/lowdraglib/gui/widget/WidgetGroup;Lcom/lowdragmc/lowdraglib/gui/modular/IUIHolder;Lnet/minecraft/world/entity/player/Player;)V", at = @At("TAIL"))
    private void atl$grabModularUiInstance(WidgetGroup mainGroup, IUIHolder holder, Player entityPlayer, CallbackInfo ci){
        Gtceu.modularUis.add(new WeakReference<>((ModularUI) (Object) this));
    }

    @Override
    public void atl$setEntityPlayer(Player player) {
        this.entityPlayer = player;
    }
}
