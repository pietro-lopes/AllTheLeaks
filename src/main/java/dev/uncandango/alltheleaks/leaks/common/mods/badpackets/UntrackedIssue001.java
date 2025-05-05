package dev.uncandango.alltheleaks.leaks.common.mods.badpackets;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

import java.lang.invoke.VarHandle;
import java.util.Set;

@Issue(modId = "badpackets", versionRange = "[0.8.1,)", description = "Clears `ChannelRegistry#handlers` map on server stopped")
public class UntrackedIssue001 {
	public static final VarHandle HANDLERS;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearPacketsFromHandler);
	}

	static {
		HANDLERS = ReflectionHelper.getFieldFromClass(ChannelRegistry.class, "handlers", Set.class, false);
		// runtime integrity check
		var dummy = ChannelRegistry.PLAY_C2S;
	}

	private void clearPacketsFromHandler(ServerStoppedEvent event) {
		((Set) HANDLERS.get(ChannelRegistry.PLAY_C2S)).clear();
	}
}
