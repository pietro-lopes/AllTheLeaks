package dev.uncandango.alltheleaks.leaks.common.mods.tfcthermaldeposits;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppedEvent;
import tfcthermaldeposits.TDForgeEventHandler;

import java.lang.invoke.VarHandle;

@Issue(modId = "tfcthermaldeposits", versionRange = "[1.3.2,)",
	description = "Clears `TDForgeEventHandler#worldLevel` on server stop")
public class UntrackedIssue001 {
	public static final VarHandle WORLD_LEVEL;

	static {
		WORLD_LEVEL = ReflectionHelper.getFieldFromClass(TDForgeEventHandler.class, "worldLevel", WorldGenLevel.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearLevelFromEvent);
	}

	private void clearLevelFromEvent(ServerStoppedEvent event) {
		WORLD_LEVEL.set((Object) null);
	}
}
