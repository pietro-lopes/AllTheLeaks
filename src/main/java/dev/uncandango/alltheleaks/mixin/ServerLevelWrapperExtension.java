package dev.uncandango.alltheleaks.mixin;

import net.minecraft.server.level.ServerLevel;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public interface ServerLevelWrapperExtension {
    Set<ServerLevel> atl$unloadedLevels = Collections.newSetFromMap(new WeakHashMap<>());

    static boolean atl$checkIfUnloaded(ServerLevel level) {
        return atl$unloadedLevels.contains(level);
    }

}
