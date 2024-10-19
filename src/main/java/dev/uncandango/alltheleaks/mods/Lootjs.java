package dev.uncandango.alltheleaks.mods;

import com.almostreliable.lootjs.LootModificationsAPI;

public interface Lootjs {
    static void run() {
        LootModificationsAPI.reload();
    }
}
