package dev.uncandango.alltheleaks.leaks.common.mods.sereneseasons;

import dev.uncandango.alltheleaks.annotation.Issue;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import sereneseasons.season.SeasonHandler;

@Issue(modId = "sereneseasons", versionRange = "[10.1.0.1,)")
public class UntrackedIssue001 {

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearLevelFromSeasonHandler);
	}

	private void clearLevelFromSeasonHandler(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) return;
		SeasonHandler.lastDayTimes.remove(event.getLevel());
		SeasonHandler.updateTicks.remove(event.getLevel());
	}
}
