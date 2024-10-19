package dev.uncandango.alltheleaks.mixin.core.main;


import dev.uncandango.alltheleaks.utils.DebugHelper;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SynchedEntityData.DataValue.class)
public class SynchedEntityDataMixin {
    @Inject(method = "create", at = @At("HEAD"))
    private static <T> void atl$breakpoint(EntityDataAccessor<T> arg, T object, CallbackInfoReturnable<SynchedEntityData.DataValue<T>> cir){
        if (object instanceof ItemStack stack) {
            var entity = stack.getEntityRepresentation();
            if (entity != null) {
                var level = entity.level();
                if (level.isClientSide()){
                    DebugHelper.pauseOnIde();
                }
            }
        }
    }

}
