package dev.uncandango.alltheleaks.leaks.common.mods.forge;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.ConnectionAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.FakePlayerNetHandlerAccessor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppedEvent;

@Issue(modId = "forge", versionRange = "[47.2,)", mixins = {"accessor.FakePlayerNetHandlerAccessor", "accessor.ConnectionAccessor"},
	description = "Clears packet listener on server stop for FakePlayers")
public class UntrackedIssue002 {
	public UntrackedIssue002() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearPacketListener);
	}

	private void clearPacketListener(ServerStoppedEvent event) {
		if (FakePlayerNetHandlerAccessor.getDUMMY_CONNECTION() instanceof ConnectionAccessor accessor) {
			accessor.setPacketListener(null);
		}
	}
}
