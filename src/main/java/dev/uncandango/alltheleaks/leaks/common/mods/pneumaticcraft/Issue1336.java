package dev.uncandango.alltheleaks.leaks.common.mods.pneumaticcraft;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.EventKey;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;

@Issue(issueId = "#1336", modId = "pneumaticcraft", versionRange = "(,8.1.0]", mixins = "main.DroneEntityMixin")
public class Issue1336 {

	public Issue1336() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::unregisterDrones);
	}

	private void unregisterDrones(EntityLeaveLevelEvent event) {
		if (event.getEntity().level().isClientSide()) {
			return;
		}
		if (event.getEntity() instanceof EventKey obj) {
			var key = obj.atl$getKeyMap().get("onSemiblockEvent");
			if (key != null) {
				NeoForge.EVENT_BUS.unregister(key);
			} else {
				AllTheLeaks.LOGGER.warn("Event Registry key not found for entity {}", obj);
			}
		}
	}
}
