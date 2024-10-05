package dev.uncandango.alltheleaks.leaks.client.mods.iceberg;

import com.anthonyhilyard.iceberg.util.EntityCollector;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.CustomItemRendererAccessor;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "iceberg", versionRange = "[1.2.8,)", mixins = "accessor.CustomItemRendererAccessor")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearEntitiesOnLevelUnload);
	}

	private void clearEntitiesOnLevelUnload(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()){
			CustomItemRendererAccessor.setWolf(null);
			CustomItemRendererAccessor.setHorse(null);
			CustomItemRendererAccessor.setEntity(null);
			CustomItemRendererAccessor.setArmorStand(null);
		}
	}

}
