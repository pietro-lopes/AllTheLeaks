package dev.uncandango.alltheleaks.leaks.client.mods.journeymap;

import dev.uncandango.alltheleaks.annotation.Issue;
import journeymap.common.util.PlayerRadarManager;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

@Issue(modId = "journeymap", versionRange = "[1.21.1-6.0.0-beta.28,)")
public class UntrackedIssue003 {
	public UntrackedIssue003() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearPlayersWithUnloadingLevel);
	}

	static {
		var dummy = PlayerRadarManager.getInstance().getPlayers();
	}

	private void clearPlayersWithUnloadingLevel(LevelEvent.Unload event){
		PlayerRadarManager.getInstance().getPlayers().entrySet().removeIf(entry -> entry.getValue() != null && entry.getValue().level() == event.getLevel());
	}
}
