package dev.uncandango.alltheleaks.mixin.core.main;

import com.jozufozu.flywheel.util.WorldAttached;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = WorldAttached.class, remap = false)
public class WorldAttachedMixin<T> {

    @Inject(method = "put", at = @At("HEAD"), cancellable = true)
    void atl$stopInjectingOldWorld(LevelAccessor world, T entry, CallbackInfo ci) {
        if (world != Minecraft.getInstance().level) ci.cancel();
    }
}
