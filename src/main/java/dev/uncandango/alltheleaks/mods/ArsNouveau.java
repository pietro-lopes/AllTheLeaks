package dev.uncandango.alltheleaks.mods;

import com.hollingsworth.arsnouveau.api.loot.DungeonLootTables;
import com.hollingsworth.arsnouveau.client.gui.GuiEntityInfoHUD;
import dev.uncandango.alltheleaks.mixin.core.accessor.CameraControllerAccessor;

public interface ArsNouveau {
    static void run() {
        // TODO: Fix Ars Nouveau Clone Event memory leak
        DungeonLootTables.CASTER_TOMES.clear();
        CameraControllerAccessor.atl$setCameraStorage(null);
        GuiEntityInfoHUD.lastHovered = null;
    }
}
