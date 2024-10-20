package dev.uncandango.alltheleaks.mods;

import net.minecraft.world.level.LevelAccessor;

public interface Flywheel {
    static void unloadWorld(LevelAccessor level) {
//        InstancedRenderDispatcher.getInstanceWorld(level).delete();
//        WorldAttached.invalidateWorld(level);
    }
}
