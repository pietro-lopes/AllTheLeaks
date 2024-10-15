package dev.uncandango.alltheleaks.leaks.client.mods.iceberg;

import com.anthonyhilyard.iceberg.util.EntityCollector;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "iceberg", versionRange = "[1.2.8,)")
public class Issue76 {
	private static final VarHandle WRAPPED_LEVELS_MAP;
	private static final VarHandle ENTITY_CACHE;

	public Issue76() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearMaps);
	}

	private void clearMaps(LevelEvent.Unload event){
		if (event.getLevel().isClientSide()) {
			var map1 = (Map) WRAPPED_LEVELS_MAP.get();
			map1.clear();
			var map2 = (Map) ENTITY_CACHE.get();
			map2.clear();
		}
	}

	static {
		WRAPPED_LEVELS_MAP = ReflectionHelper.getFieldFromClass(EntityCollector.class, "wrappedLevelsMap", Map.class, true);
		ENTITY_CACHE = ReflectionHelper.getFieldFromClass(EntityCollector.class, "entityCache", Map.class, true);
	}
}
