package dev.uncandango.alltheleaks.mods;

import appeng.integration.abstraction.IJEI;
import appeng.integration.abstraction.JEIFacade;
import dev.uncandango.alltheleaks.AllTheLeaks;
import net.minecraftforge.fml.ModList;

public interface Ae2 {
    static void run() {
        if (ModList.get().isLoaded("jei")) {
            // JEI
            AllTheLeaks.LOGGER.debug("Cleaning AE2: JEI Plugin...");
            JEIFacade.setInstance(new IJEI.Stub());
        }
    }
}
