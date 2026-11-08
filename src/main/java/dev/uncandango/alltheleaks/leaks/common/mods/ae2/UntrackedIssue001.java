package dev.uncandango.alltheleaks.leaks.common.mods.ae2;

import appeng.util.FakePlayer;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;
import java.util.WeakHashMap;

@Issue(modId = "ae2", versionRange = "[15.0.10,)")
public class UntrackedIssue001 {
	public static final VarHandle FAKE_PLAYERS;

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::onWorldUnload);
	}

	static {
		FAKE_PLAYERS = ReflectionHelper.getFieldFromClass(FakePlayer.class, "FAKE_PLAYERS", WeakHashMap.class, true);
	}

	private void onWorldUnload(LevelEvent.Unload event) {
		if (event.getLevel() instanceof ServerLevel level) {
			((WeakHashMap<Level,?>)FAKE_PLAYERS.get()).remove(level);
		}
	}
}
