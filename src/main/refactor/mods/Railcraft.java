package dev.uncandango.alltheleaks.mods;

import mods.railcraft.charge.ChargeNetworkImpl;
import mods.railcraft.charge.ChargeProviderImpl;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;

public interface Railcraft {
    @SuppressWarnings("unchecked")
    static void run() {
        try {
            var field = ChargeProviderImpl.DISTRIBUTION.getClass().getDeclaredField("networks");
            field.setAccessible(true);
            Map<ServerLevel, ChargeNetworkImpl> networks = (Map<ServerLevel, ChargeNetworkImpl>) field.get(ChargeProviderImpl.DISTRIBUTION);
            networks.clear();
        } catch (Exception ignored) {
        }
    }
}
