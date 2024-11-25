package dev.uncandango.alltheleaks.leaks.client.mods.iceberg;

import com.anthonyhilyard.iceberg.util.EntityCollector;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "iceberg", versionRange = "[1.1.10,)", description = "Unload client level from `EntityCollector#wrappedLevelsMap`")
public class UntrackedIssue001 {
	public static final VarHandle WRAPPED_LEVELS_MAP;

	static {
		WRAPPED_LEVELS_MAP = ReflectionHelper.getFieldFromClass(EntityCollector.class, "wrappedLevelsMap", Map.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearUnloadedLevel);
	}

	private void clearUnloadedLevel(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
			var levelsMap = (Map) WRAPPED_LEVELS_MAP.get();
			levelsMap.remove(event.getLevel());
		}
	}
}
