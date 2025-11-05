package dev.uncandango.alltheleaks.leaks.client.mods.transfer_labels;

import com.buuz135.transfer_labels.storage.client.LabelClientStorage;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.neoforged.neoforge.common.NeoForge;

@Issue(modId = "transfer_labels", versionRange = "[0.1.0,0.1.7)")
public class Issue7 {
	public Issue7() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearMapOnRenderUpdate);
	}

	static {
		var dummy = LabelClientStorage.LABELS;
	}

	private void clearMapOnRenderUpdate(UpdateableLevel.RenderEnginesUpdated event) {
		LabelClientStorage.LABELS.clear();
	}
}
