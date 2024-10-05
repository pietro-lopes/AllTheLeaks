package dev.uncandango.alltheleaks.leaks.client.mods.jade;

import com.google.common.cache.Cache;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import snownee.jade.JadeClient;
import snownee.jade.impl.ObjectDataCenter;

import java.lang.invoke.VarHandle;

@Issue(modId = "jade", versionRange = "[15.1.8,)")
public class UntrackedIssue001 {
	private static final VarHandle HIDE_MOD_NAME;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearAccessor);
		gameBus.addListener(this::clearAccessorFromClone);
	}

	private void clearAccessor(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
			ObjectDataCenter.set(null);
			var cache = (Cache)HIDE_MOD_NAME.get();
			cache.invalidateAll();
		}
	}

	private void clearAccessorFromClone(ClientPlayerNetworkEvent.Clone event) {
		ObjectDataCenter.set(null);
	}

	static {
		HIDE_MOD_NAME = ReflectionHelper.getFieldFromClass(JadeClient.class, "hideModName", Cache.class, true);
	}
}
