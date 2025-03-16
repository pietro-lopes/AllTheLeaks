package dev.uncandango.alltheleaks.leaks.common.mods.phosphophyllite;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.roguelogix.phosphophyllite.config.ConfigManager;
import net.roguelogix.phosphophyllite.modular.tile.IIsTickingTracker;

import java.lang.invoke.VarHandle;

@Issue(modId = "phosphophyllite", versionRange = "[0.7.0-alpha,)", description = "Updates `ConfigManager#players` on player clone and clears `IIsTickingTracker$Module#isTickingMap` on server stop")
public class UntrackedIssue001 {
	public static final VarHandle IS_TICKING_MAP;
	public static final VarHandle PLAYERS;

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearOnServerStopped);
		gameBus.addListener(this::updatePlayerListOnClone);
	}

	private void updatePlayerListOnClone(PlayerEvent.Clone event) {
		((ObjectArrayList)PLAYERS.get()).remove(event.getOriginal());
		((ObjectArrayList)PLAYERS.get()).add(event.getEntity());
	}

	static {
		IS_TICKING_MAP = ReflectionHelper.getFieldFromClass(IIsTickingTracker.Module.class, "isTickingMap", Object2ObjectMap.class, true);
		PLAYERS = ReflectionHelper.getFieldFromClass(ConfigManager.class, "players", ObjectArrayList.class, true);
	}

	private void clearOnServerStopped(ServerStoppedEvent event) {
		((Object2ObjectMap) IS_TICKING_MAP.get()).clear();
	}
}
