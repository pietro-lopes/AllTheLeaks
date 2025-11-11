package dev.uncandango.alltheleaks.leaks.client.mods.flywheel;

import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.concurrent.atomic.AtomicInteger;

@Issue(modId = "flywheel", versionRange = "*", extraModDep = "embeddium", extraModDepVersions = "*", mixins = {"main.LevelAttachedMixin"})
public class UntrackedIssue001 {
	public static final AtomicInteger lastUnloadedLevelHash = new AtomicInteger();

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(EventPriority.HIGHEST, this::getHashOfUnloadedClientLevels);
	}

	private void getHashOfUnloadedClientLevels(LevelEvent.Unload event) {
		if (event.getLevel() instanceof ClientLevel cl) {
			lastUnloadedLevelHash.set(System.identityHashCode(cl));
			//LevelAttachedExtension.atl$unloadedLevels.add(System.identityHashCode(cl));
//			AllTheLeaks.LOGGER.info("Unload event received for {}@{} ({})",cl.getClass().getSimpleName(), Integer.toHexString(System.identityHashCode(cl)),
//				cl.dimension().location());
		}
	}
}
