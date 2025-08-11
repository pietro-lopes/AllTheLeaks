package dev.uncandango.alltheleaks.leaks.common.mods.integratedtunnels;

import com.google.common.cache.Cache;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppedEvent;
import org.cyclops.integratedtunnels.core.TunnelHelpers;

import java.lang.invoke.VarHandle;

@Issue(modId = "integratedtunnels", versionRange = "[1.8.42,)")
public class UntrackedIssue001 {
	private static final VarHandle CACHE_INV_CHECKS;

	static {
		CACHE_INV_CHECKS = ReflectionHelper.getFieldFromClass(TunnelHelpers.class, "CACHE_INV_CHECKS", Cache.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearInventoryTicks);
	}

	private void clearInventoryTicks(ServerStoppedEvent event) {
		((Cache)CACHE_INV_CHECKS.get()).invalidateAll();
	}
}
