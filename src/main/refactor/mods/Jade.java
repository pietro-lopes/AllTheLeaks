package dev.uncandango.alltheleaks.mods;

import snownee.jade.impl.ObjectDataCenter;

public interface Jade {
    static void run() {
        ObjectDataCenter.set(null);
    }
}
