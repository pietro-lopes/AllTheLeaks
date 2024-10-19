package dev.uncandango.alltheleaks.mods;

public interface AmbientSounds {
    static void run() {
        team.creative.ambientsounds.AmbientSounds.TICK_HANDLER.environment = null;
    }
}
