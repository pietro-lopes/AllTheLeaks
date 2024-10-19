package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.common.event.CuriosEventHandler;

@Pseudo
@Mixin(value = CuriosEventHandler.class, remap = false)
public class CuriosEventHandlerMixin {
    @WrapOperation(method = "playerClone", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;revive()V"))
    private void atl$properReviveCaps(Player instance, Operation<Void> original){
        instance.reviveCaps();
    }

    @Inject(method = "playerClone", at = @At(value = "RETURN"))
    private void atl$invalidateCaps(PlayerEvent.Clone evt, CallbackInfo ci){
        evt.getOriginal().invalidateCaps();
    }
}
