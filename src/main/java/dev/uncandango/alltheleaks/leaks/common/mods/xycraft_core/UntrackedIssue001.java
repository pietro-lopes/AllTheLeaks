package dev.uncandango.alltheleaks.leaks.common.mods.xycraft_core;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import tv.soaryn.xycraft.core.utils.FakePlayerUtils;

import java.lang.invoke.VarHandle;
import java.util.WeakHashMap;

@Issue(modId = "xycraft_core", versionRange = "[0.7.45,)")
public class UntrackedIssue001 {
	public static final VarHandle FAKE_PLAYERS;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearOnLevelUnloaded);
	}

	static {
		FAKE_PLAYERS = ReflectionHelper.getFieldFromClass(FakePlayerUtils.class, "FAKE_PLAYERS", WeakHashMap.class, true);
	}

	private void clearOnLevelUnloaded(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) return;
		((WeakHashMap)FAKE_PLAYERS.get()).remove(event.getLevel());
	}
}
