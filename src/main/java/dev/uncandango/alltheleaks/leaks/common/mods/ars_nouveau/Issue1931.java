package dev.uncandango.alltheleaks.leaks.common.mods.ars_nouveau;

import com.hollingsworth.arsnouveau.api.registry.AlakarkinosConversionRegistry;
import dev.uncandango.alltheleaks.annotation.Issue;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

@Issue(issueId = "#1931", modId = "ars_nouveau", versionRange = "[5.8.4,)")
public class Issue1931 {
	public Issue1931() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearParamOnUnload);
	}

	static {
		var dummy = AlakarkinosConversionRegistry.LOOT_PARAMS;
	}

	private void clearParamOnUnload(LevelEvent.Unload event) {
		if (!event.getLevel().isClientSide()) {
			if (AlakarkinosConversionRegistry.LOOT_PARAMS != null) {
				if (AlakarkinosConversionRegistry.LOOT_PARAMS.getLevel() == event.getLevel()) {
					AlakarkinosConversionRegistry.LOOT_PARAMS = null;
				}
			}
		}
	}
}
