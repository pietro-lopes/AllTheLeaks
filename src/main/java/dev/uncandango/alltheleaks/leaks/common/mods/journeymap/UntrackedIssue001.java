package dev.uncandango.alltheleaks.leaks.common.mods.journeymap;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import journeymap.common.Journeymap;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppedEvent;

import java.lang.invoke.VarHandle;

@Issue(modId = "journeymap", versionRange = "*")
public class UntrackedIssue001 {
	private static final VarHandle SERVER;

	static {
		SERVER = ReflectionHelper.getFieldFromClass(Journeymap.class, "server", MinecraftServer.class, false);
	}
	
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearServerFromInstance);
	}

	private void clearServerFromInstance(ServerStoppedEvent event) {
		var journeyMapInstance = Journeymap.getInstance();
		SERVER.set(journeyMapInstance, null);
	}
}
