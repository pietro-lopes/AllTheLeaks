package dev.uncandango.alltheleaks.mods;

import com.legacy.blue_skies.client.audio.ambient.DungeonAmbientSoundHandler;
import dev.uncandango.alltheleaks.mixin.core.accessor.SkiesClientEventsAccessor;
import net.minecraft.client.Minecraft;

public interface Blueskies {
    static void run() {
        SkiesClientEventsAccessor.atl$setDungeonAmbientSoundHandler(new DungeonAmbientSoundHandler(null, Minecraft.getInstance().getSoundManager()));
    }
}
