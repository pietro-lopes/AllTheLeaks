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

/*
  package dev.uncandango.alltheleaks.leaks.client.mods.ae2;

import appeng.integration.abstraction.IJEI;
import appeng.integration.abstraction.JEIFacade;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.plugins.ATLJeiPlugin;
import net.minecraftforge.common.MinecraftForge;

@Issue(modId = "ae2", versionRange = "[15.2.1,)", extraModDep = "jei", extraModDepVersions = "[15.4.0.9,)",
	description = """

				""")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearOnRuntimeUnavailable);
	}

	private void clearOnRuntimeUnavailable(ATLJeiPlugin.RuntimeUnavailableEvent event) {
		JEIFacade.setInstance(new IJEI.Stub());
	}
}


 */