package dev.uncandango.alltheleaks.mods;

import tfcthermaldeposits.TDForgeEventHandler;

public interface Tfcthermaldeposits {
    static void run() {
        TDForgeEventHandler.worldLevel = null;
    }
}
