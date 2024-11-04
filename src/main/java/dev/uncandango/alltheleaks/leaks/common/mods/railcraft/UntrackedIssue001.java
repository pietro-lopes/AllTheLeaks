package dev.uncandango.alltheleaks.leaks.common.mods.railcraft;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import mods.railcraft.charge.ChargeProviderImpl;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "railcraft", versionRange = "[1.1.2,1.1.6]", description = "Clears level from `ChargeProviderImpl.DISTRIBUTION#networks` on level unload")
public class UntrackedIssue001 {
	public static final VarHandle NETWORKS;

	static {
		NETWORKS = ReflectionHelper.getFieldFromClass(ChargeProviderImpl.DISTRIBUTION.getClass(), "networks", Map.class, false);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearNetwork);
	}

	@SuppressWarnings("rawtypes")
	private void clearNetwork(LevelEvent.Unload event) {
		if (!event.getLevel().isClientSide()) {
			((Map) NETWORKS.get(ChargeProviderImpl.DISTRIBUTION)).remove(event.getLevel());
		}
	}
}
