package dev.uncandango.alltheleaks.leaks.client.mods.jade;

import dev.uncandango.alltheleaks.annotation.Issue;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import snownee.jade.impl.ObjectDataCenter;

@Issue(modId = "jade", versionRange = "[15.1.8,)")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearAccessor);
		gameBus.addListener(this::clearAccessorFromClone);
	}

	private void clearAccessor(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
			ObjectDataCenter.set(null);
		}
	}

	private void clearAccessorFromClone(ClientPlayerNetworkEvent.Clone event) {
		ObjectDataCenter.set(null);
	}
}
