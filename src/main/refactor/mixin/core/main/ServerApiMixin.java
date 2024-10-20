package dev.uncandango.alltheleaks.mixin.core.main;

import com.seibel.distanthorizons.core.api.internal.ServerApi;
import com.seibel.distanthorizons.coreapi.DependencyInjection.WorldGeneratorInjector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = ServerApi.class, remap = false)
public class ServerApiMixin {
    @Inject(method = "serverUnloadEvent", at = @At("TAIL"))
    void atl$clearWorldGenInjectors(CallbackInfo ci) {
        WorldGeneratorInjector.INSTANCE.clear();
    }
}
