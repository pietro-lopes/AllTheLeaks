package dev.uncandango.alltheleaks.mods;

import com.mrh0.createaddition.energy.network.EnergyNetworkManager;

public interface Createaddition {
    static void run() {
        EnergyNetworkManager.instances.clear();
    }
}
