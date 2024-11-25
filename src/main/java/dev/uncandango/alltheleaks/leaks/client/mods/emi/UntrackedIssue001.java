package dev.uncandango.alltheleaks.leaks.client.mods.emi;

import dev.emi.emi.runtime.EmiHistory;
import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;

@Issue(modId = "emi", versionRange = "[1.0.21,)", description = "Clear `EmiHistory` when client player cloned")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearPlayerFromHistory);
	}

	static {
		try {
			EmiHistory.class.getDeclaredMethod("clear");
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	private void clearPlayerFromHistory(ClientPlayerNetworkEvent.Clone event) {
		EmiHistory.clear();
	}
}
