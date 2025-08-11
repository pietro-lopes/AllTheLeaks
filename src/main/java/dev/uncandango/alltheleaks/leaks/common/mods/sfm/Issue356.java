package dev.uncandango.alltheleaks.leaks.common.mods.sfm;

import ca.teamdman.sfm.common.program.LimitedInputSlot;
import ca.teamdman.sfm.common.program.LimitedInputSlotObjectPool;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppedEvent;

import java.lang.invoke.VarHandle;
import java.util.Arrays;

@Issue(modId = "sfm", issueId = "#356", versionRange = "(,4.23.0]")
public class Issue356 {
	private static final VarHandle POOL;
	private static final VarHandle INDEX;
	public Issue356() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearPoolOnServerStopped);
	}

	static {
		POOL = ReflectionHelper.getFieldFromClass(LimitedInputSlotObjectPool.class, "pool", LimitedInputSlot[].class, true);
		INDEX = ReflectionHelper.getFieldFromClass(LimitedInputSlotObjectPool.class, "index", int.class, true);
	}

	private void clearPoolOnServerStopped(ServerStoppedEvent event) {
		Arrays.fill((LimitedInputSlot[])POOL.get(), null);
		INDEX.set(-1);
	}
}
