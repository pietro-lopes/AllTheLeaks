package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.railwayteam.railways.content.conductor.ConductorPossessionController;
import net.minecraft.client.multiplayer.ClientChunkCache.Storage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = ConductorPossessionController.class, remap = false)
public interface ConductorPossessionControllerAccessor {
    @Mutable
    @Accessor("cameraStorage")
    static void atl$setCameraStorage(Storage storage) {
    }

}

