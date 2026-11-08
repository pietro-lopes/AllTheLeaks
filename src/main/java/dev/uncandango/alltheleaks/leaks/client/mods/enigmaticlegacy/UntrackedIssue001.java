package dev.uncandango.alltheleaks.leaks.client.mods.enigmaticlegacy;

import com.aizistral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;

@Issue(modId = "enigmaticlegacy", versionRange = "[2.30.1,)")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearOnClone);
		gameBus.addListener(this::clearOnLogout);
	}

	static {
		var dummy = EnigmaticEventHandler.DEFERRED_TOASTS;
	}

	private void clearOnClone(ClientPlayerNetworkEvent.Clone event) {
		EnigmaticEventHandler.DEFERRED_TOASTS.put(event.getNewPlayer(), EnigmaticEventHandler.DEFERRED_TOASTS.remove(event.getOldPlayer()));
	}

	private void clearOnLogout(ClientPlayerNetworkEvent.LoggingOut event) {
		EnigmaticEventHandler.DEFERRED_TOASTS.remove(event.getPlayer());
	}
}
