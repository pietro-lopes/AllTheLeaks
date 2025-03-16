package dev.uncandango.alltheleaks.leaks.common.mods.alexsmobs;

import com.github.alexthe666.alexsmobs.event.ServerEvents;
import com.github.alexthe666.alexsmobs.world.AMWorldData;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "alexsmobs", issueId = "#2165", versionRange = "[1.22.5,)", description = "Removes unloaded server level from `ServerEvents#BEACHED_CACHALOT_WHALE_SPAWNER_MAP` and `AMWorldData#dataMap`")
public class Issue2165 {
	public static final VarHandle BEACHED_CACHALOT_WHALE_SPAWNER_MAP;
	public static final VarHandle AM_WORLD_DATA;

	static {
		BEACHED_CACHALOT_WHALE_SPAWNER_MAP = ReflectionHelper.getFieldFromClass(ServerEvents.class, "BEACHED_CACHALOT_WHALE_SPAWNER_MAP", Map.class, true);
		AM_WORLD_DATA = ReflectionHelper.getFieldFromClass(AMWorldData.class, "dataMap", Map.class, true);
	}

	public Issue2165() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearSpawnerMap);
	}

	private void clearSpawnerMap(LevelEvent.Unload event) {
		if (!event.getLevel().isClientSide()) {
			((Map) BEACHED_CACHALOT_WHALE_SPAWNER_MAP.get()).remove(event.getLevel());
			((Map) AM_WORLD_DATA.get()).remove(event.getLevel());
		}
	}
}
