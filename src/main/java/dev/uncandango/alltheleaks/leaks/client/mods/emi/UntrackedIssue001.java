package dev.uncandango.alltheleaks.leaks.client.mods.emi;

import dev.emi.emi.runtime.EmiHistory;
import dev.uncandango.alltheleaks.annotation.Issue;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;

@Issue(modId = "emi", versionRange = "[1.1.7,)")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearHistory);
	}

	private void clearHistory(ClientPlayerNetworkEvent.Clone event) {
		EmiHistory.clear();
	}
}
