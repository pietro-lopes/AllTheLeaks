package dev.uncandango.alltheleaks.utils;

import net.minecraft.network.Connection;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public interface Storage {
    Set<FakePlayer> fakePlayers = Collections.newSetFromMap(new WeakHashMap<>());
    Set<Connection> allConnections = Collections.newSetFromMap(new WeakHashMap<>());
}
