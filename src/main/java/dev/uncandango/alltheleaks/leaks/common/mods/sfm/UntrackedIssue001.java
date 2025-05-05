package dev.uncandango.alltheleaks.leaks.common.mods.sfm;

import ca.teamdman.sfm.common.watertanknetwork.WaterNetworkManager;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

import java.lang.invoke.MethodType;

@Issue(modId = "sfm", versionRange = "[4.21.0,)")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearOnServerStopped);
	}

	private void clearOnServerStopped(ServerStoppedEvent event) {
		WaterNetworkManager.clear();
	}

	static {
		var dummy = ReflectionHelper.getMethodFromClass(WaterNetworkManager.class, "clear", MethodType.methodType(void.class), true);
	}
}
