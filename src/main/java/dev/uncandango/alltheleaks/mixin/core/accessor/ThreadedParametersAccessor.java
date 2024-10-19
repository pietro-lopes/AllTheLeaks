package dev.uncandango.alltheleaks.mixin.core.accessor;

import loaderCommon.forge.com.seibel.distanthorizons.common.wrappers.worldGeneration.GlobalParameters;
import loaderCommon.forge.com.seibel.distanthorizons.common.wrappers.worldGeneration.ThreadedParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = ThreadedParameters.class, remap = false)
public interface ThreadedParametersAccessor {
    @Mutable
    @Accessor("previousGlobalParameters")
    static void atl$setPreviousGlobalParameters(GlobalParameters params) {
    }

}
