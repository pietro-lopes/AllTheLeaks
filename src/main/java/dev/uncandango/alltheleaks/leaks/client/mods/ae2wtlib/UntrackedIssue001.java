package dev.uncandango.alltheleaks.leaks.client.mods.ae2wtlib;

import de.mari_023.ae2wtlib.wct.CraftingTerminalHandler;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;

import java.lang.invoke.VarHandle;
import java.util.WeakHashMap;

@Issue(modId = "ae2wtlib", versionRange = "[15.2.3,)",
description = "Clears client cloned player from `CraftingTerminalHandler#players`")
public class UntrackedIssue001 {
	public static final VarHandle PLAYERS;

	static {
		PLAYERS = ReflectionHelper.getFieldFromClass(CraftingTerminalHandler.class, "players", WeakHashMap.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearPlayers);
	}

	@SuppressWarnings("rawtypes")
	private void clearPlayers(ClientPlayerNetworkEvent.Clone event) {
		((WeakHashMap)PLAYERS.get()).remove(event.getOldPlayer());
	}
}
