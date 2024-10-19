package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.hollingsworth.arsnouveau.common.camera.CameraController;
import net.minecraft.client.multiplayer.ClientChunkCache.Storage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = CameraController.class, remap = false)
public interface CameraControllerAccessor {
    @Mutable
    @Accessor("cameraStorage")
    static void atl$setCameraStorage(Storage storage) {
    }

}

