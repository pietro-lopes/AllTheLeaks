package dev.uncandango.alltheleaks.mods;

import com.github.alexthe666.iceandfire.pathfinding.raycoms.WorldEventContext;

public interface IceAndFire {
    static void run() {
        WorldEventContext.INSTANCE.clientLevel = null;
        WorldEventContext.INSTANCE.clientPlayer = null;
    }
}
