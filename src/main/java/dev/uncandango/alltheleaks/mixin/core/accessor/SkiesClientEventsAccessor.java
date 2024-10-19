package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.legacy.blue_skies.client.audio.ambient.DungeonAmbientSoundHandler;
import com.legacy.blue_skies.client.events.SkiesClientEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = SkiesClientEvents.class, remap = false)
public interface SkiesClientEventsAccessor {
    @Mutable
    @Accessor("dungeonAmbientSoundHandler")
    static void atl$setDungeonAmbientSoundHandler(DungeonAmbientSoundHandler handler) {
    }

}
