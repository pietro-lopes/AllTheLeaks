package dev.uncandango.alltheleaks.leaks.common.mods.sereneseasons;

import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import sereneseasons.season.SeasonHandler;

@Issue(modId = "sereneseasons", versionRange = "[9.1.0.0,)", description = "Clears `SeasonHandler#updateTicks/lastDayTimes` on server level unload")
public class UntrackedIssue001 {
	static {
		// Dummy to validate variable calls
		var dummy1 = SeasonHandler.updateTicks;
		var dummy2 = SeasonHandler.lastDayTimes;
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearLevelFromMaps);
	}

	private void clearLevelFromMaps(LevelEvent.Unload event) {
		if (!event.getLevel().isClientSide()) {
			SeasonHandler.updateTicks.remove((Level) event.getLevel());
			SeasonHandler.lastDayTimes.remove((Level) event.getLevel());
		}
	}
}
