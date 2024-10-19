package dev.uncandango.alltheleaks.mods;

import dev.uncandango.alltheleaks.mixin.core.accessor.ClientMagicDataAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.SpellBarOverlayAccessor;

public interface IronsSpells {
    static void run() {
        SpellBarOverlayAccessor.atl$setLastSelection(null);
        ClientMagicDataAccessor.atl$setSpellSelectionManager(null);
    }
}
