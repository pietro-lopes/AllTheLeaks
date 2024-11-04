package dev.uncandango.alltheleaks.leaks.common.mods.citadel;

import com.github.alexthe666.citadel.server.world.CitadelServerData;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppingEvent;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "citadel", versionRange = "[2.5.4,)", description = "Clears server from `CitadelServerData#dataMap` on stop")
public class UntrackedIssue001 {
	public static final VarHandle DATA_MAP;

	static {
		DATA_MAP = ReflectionHelper.getFieldFromClass(CitadelServerData.class, "dataMap", Map.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearServerFromNetworkMap);
	}

	@SuppressWarnings("rawtypes")
	private void clearServerFromNetworkMap(ServerStoppingEvent event) {
		((Map) DATA_MAP.get()).remove(event.getServer());
	}
}
