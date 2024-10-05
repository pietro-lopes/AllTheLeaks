package dev.uncandango.alltheleaks.leaks.client.mods.mekanism;

import dev.uncandango.alltheleaks.annotation.Issue;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import static mekanism.common.Mekanism.playerState;

@Issue(modId = "mekanism", issueId = "#8221", versionRange = "(,10.7.5]")
public class Issue8221 {
	public Issue8221() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearPlayerState);
	}

	private void clearPlayerState(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
			playerState.init(null);
		}
	}
}
