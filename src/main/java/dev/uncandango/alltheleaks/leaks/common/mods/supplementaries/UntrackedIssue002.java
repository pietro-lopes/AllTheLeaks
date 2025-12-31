package dev.uncandango.alltheleaks.leaks.common.mods.supplementaries;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.mehvahdjukaar.supplementaries.common.block.tiles.EndermanSkullBlockTile;
import net.mehvahdjukaar.supplementaries.common.items.crafting.WeatheredMapRecipe;
import net.mehvahdjukaar.supplementaries.common.misc.map_data.ColoredMapHandler;
import net.mehvahdjukaar.supplementaries.common.worldgen.WaySignStructure;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

@Issue(modId = "supplementaries", versionRange = "[1.21-3.5.0,)")
public class UntrackedIssue002 {
	public static final MethodHandle CLEAR_CACHE;
	public UntrackedIssue002() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearRemaining);
	}

	static {
		var clazz = ReflectionHelper.getClass("net.mehvahdjukaar.supplementaries.common.worldgen.RoadSignStructure");
		CLEAR_CACHE = ReflectionHelper.getMethodFromClass(clazz, "clearCache", MethodType.methodType(void.class), true);
	}

	private void clearRemaining(ServerStoppedEvent event) {
		try {
			CLEAR_CACHE.invoke();
		} catch (Throwable e) {
			AllTheLeaks.LOGGER.error("Error while invoking Supplementaries RoadSignStructure#clearCache", e);
		}
	}
}
