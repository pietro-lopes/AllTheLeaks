package dev.uncandango.alltheleaks.leaks.client.mods.botania;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import vazkii.botania.common.block.block_entity.red_string.RedStringInterceptorBlockEntity;
import vazkii.botania.common.handler.ManaNetworkHandler;

import java.lang.invoke.VarHandle;
import java.util.Map;
import java.util.Set;

@Issue(modId = "botania", versionRange = "*")
public class UntrackedIssue001 {
	public static final VarHandle MANA_POOLS;
	public static final VarHandle MANA_COLLECTORS;
	public static final VarHandle INTERCEPTORS;

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearOnLevelUnload);
		gameBus.addListener(this::clearOnServerStopped);
	}

	static {
		var dummy = ManaNetworkHandler.instance;
		MANA_POOLS = ReflectionHelper.getFieldFromClass(ManaNetworkHandler.class, "manaPools", Map.class, false);
		MANA_COLLECTORS = ReflectionHelper.getFieldFromClass(ManaNetworkHandler.class, "manaCollectors", Map.class, false);
		INTERCEPTORS = ReflectionHelper.getFieldFromClass(RedStringInterceptorBlockEntity.class, "interceptors", Set.class, true);
	}

	private void clearOnServerStopped(ServerStoppedEvent event) {
		((Set) INTERCEPTORS.get()).clear();
	}

	private void clearOnLevelUnload(LevelEvent.Unload event) {
		synchronized (UntrackedIssue001.class) {
			((Map)MANA_POOLS.get(ManaNetworkHandler.instance)).remove(event.getLevel());
			((Map)MANA_COLLECTORS.get(ManaNetworkHandler.instance)).remove(event.getLevel());
		}
	}
}
