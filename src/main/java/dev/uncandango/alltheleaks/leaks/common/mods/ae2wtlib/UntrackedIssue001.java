package dev.uncandango.alltheleaks.leaks.common.mods.ae2wtlib;

import de.mari_023.ae2wtlib.wct.CraftingTerminalHandler;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;

import java.lang.invoke.VarHandle;
import java.util.WeakHashMap;

@Issue(modId = "ae2wtlib", versionRange = "[15.2.3,)")
public class UntrackedIssue001 {
	public static final VarHandle PLAYERS;

	static {
		PLAYERS = ReflectionHelper.getFieldFromClass(CraftingTerminalHandler.class, "players", WeakHashMap.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearPlayers);
		gameBus.addListener(this::clearPlayersLogout);
	}

	private void clearPlayers(PlayerEvent.Clone event) {
		((WeakHashMap)PLAYERS.get()).remove(event.getOriginal());
	}

	private void clearPlayersLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		((WeakHashMap)PLAYERS.get()).remove(event.getEntity());
	}
}
