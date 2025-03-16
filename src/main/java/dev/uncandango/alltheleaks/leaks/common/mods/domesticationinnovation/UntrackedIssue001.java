package dev.uncandango.alltheleaks.leaks.common.mods.domesticationinnovation;

import com.github.alexthe668.domesticationinnovation.server.CommonProxy;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "domesticationinnovation", versionRange = "[1.7.0,)", description = "Clears `CommonProxy#COLLAR_TICK_TRACKER_MAP` on level unload")
public class UntrackedIssue001 {
	public static final VarHandle COLLAR_TICK_TRACKER_MAP;

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearOnLevelUnload);
	}

	static {
		COLLAR_TICK_TRACKER_MAP = ReflectionHelper.getFieldFromClass(CommonProxy.class, "COLLAR_TICK_TRACKER_MAP", Map.class, true);
	}

	private void clearOnLevelUnload(LevelEvent.Unload event) {
		if (!event.getLevel().isClientSide()) {
			((Map) COLLAR_TICK_TRACKER_MAP.get()).remove(event.getLevel());
		}
	}
}
