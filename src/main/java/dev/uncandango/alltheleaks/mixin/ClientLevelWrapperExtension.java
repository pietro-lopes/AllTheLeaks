package dev.uncandango.alltheleaks.mixin;

import net.minecraft.client.multiplayer.ClientLevel;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public interface ClientLevelWrapperExtension {
    Set<ClientLevel> atl$unloadedLevels = Collections.newSetFromMap(new WeakHashMap<>());

    static boolean atl$checkIfUnloaded(ClientLevel level) {
        return atl$unloadedLevels.contains(level);
    }

}
