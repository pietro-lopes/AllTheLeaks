package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.utils.StorageClient;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract boolean isEmpty();

    @Inject(method = "setEntityRepresentation", at = @At("HEAD"), cancellable = true)
    private void atl$checkEmpty(Entity arg, CallbackInfo ci){
        if (this.isEmpty()) ci.cancel();
        if (arg != null && arg.level().isClientSide()){
            StorageClient.stackWithRepresentation.add(new WeakReference<>((ItemStack)(Object)this));
        }

    }
}
