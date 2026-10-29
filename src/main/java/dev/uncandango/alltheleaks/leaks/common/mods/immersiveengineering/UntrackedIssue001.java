package dev.uncandango.alltheleaks.leaks.common.mods.immersiveengineering;

import blusunrize.immersiveengineering.api.wires.redstone.WirelessRedstoneHandler;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "immersiveengineering", versionRange = "[12.2.0-187,)")
public class UntrackedIssue001 {
	public static final VarHandle LEVEL_CACHE;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::onLevelUnload);
	}

	static {
		LEVEL_CACHE = ReflectionHelper.getFieldFromClass(WirelessRedstoneHandler.class, "LEVEL_CACHE", Map.class, true);
	}

	private void onLevelUnload(LevelEvent.Unload event) {
		if (event.getLevel() instanceof ServerLevel level) {
			((Map<ServerLevel,WirelessRedstoneHandler>)LEVEL_CACHE.get()).remove(level);
		}
	}
}
