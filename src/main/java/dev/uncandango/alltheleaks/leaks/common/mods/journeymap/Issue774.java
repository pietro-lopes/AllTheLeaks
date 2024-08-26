package dev.uncandango.alltheleaks.leaks.common.mods.journeymap;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.JourneymapAccessor;
import journeymap.common.Journeymap;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

@Issue(issueId = "#774", modId = "journeymap", versionRange = "(,1.21-6.0.0-beta.20]", mixins = "accessor.JourneymapAccessor")
public class Issue774 {
	public Issue774() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearServerFromInstance);
	}

	private void clearServerFromInstance(ServerStoppedEvent event) {
		if (Journeymap.getInstance() instanceof JourneymapAccessor accessor) {
			accessor.atl$setServer(null);
		}
	}
}
