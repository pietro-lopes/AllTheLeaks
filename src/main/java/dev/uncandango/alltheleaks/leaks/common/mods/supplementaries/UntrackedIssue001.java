package dev.uncandango.alltheleaks.leaks.common.mods.supplementaries;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.mehvahdjukaar.supplementaries.common.block.tiles.EndermanSkullBlockTile;
import net.mehvahdjukaar.supplementaries.common.items.crafting.WeatheredMapRecipe;
import net.mehvahdjukaar.supplementaries.common.misc.map_data.ColoredMapHandler;
import net.mehvahdjukaar.supplementaries.common.worldgen.WaySignStructure;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

import java.lang.invoke.MethodType;

@Issue(modId = "supplementaries", versionRange = "[1.21-3.1.8,)")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearRemaining);
	}

	static {
		var dummy = ReflectionHelper.getMethodFromClass(WeatheredMapRecipe.class, "onWorldUnload", MethodType.methodType(void.class), true);
		dummy = ReflectionHelper.getMethodFromClass(WaySignStructure.class, "clearCache", MethodType.methodType(void.class), true);
		dummy = ReflectionHelper.getMethodFromClass(EndermanSkullBlockTile.class, "clearCache", MethodType.methodType(void.class), true);
		dummy = ReflectionHelper.getMethodFromClass(ColoredMapHandler.class, "clearIdCache", MethodType.methodType(void.class), true);
	}

	private void clearRemaining(ServerStoppedEvent event) {
		WeatheredMapRecipe.onWorldUnload();
		WaySignStructure.clearCache();
		EndermanSkullBlockTile.clearCache();
		ColoredMapHandler.clearIdCache();
	}
}
