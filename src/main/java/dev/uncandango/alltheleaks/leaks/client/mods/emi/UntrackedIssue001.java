package dev.uncandango.alltheleaks.leaks.client.mods.emi;

import dev.emi.emi.runtime.EmiHistory;
import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;

@Issue(modId = "emi", versionRange = "[1.0.21,)", description = "Clears `EmiHistory` when client player cloned or logout")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearPlayerFromHistory);
		gameBus.addListener(this::clearPlayerFromHistoryOnLogout);
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

	private void clearPlayerFromHistoryOnLogout(ClientPlayerNetworkEvent.LoggingOut event) {
		EmiHistory.clear();
	}
}
