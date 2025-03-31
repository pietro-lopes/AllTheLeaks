package dev.uncandango.alltheleaks.leaks.client.mods.travelersbackpack;

import com.tiviacz.travelersbackpack.client.model.BackpackLayerModel;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraftforge.common.MinecraftForge;

@Issue(modId = "travelersbackpack", versionRange = "[9.1.16,9.1.33]", description = "Clears entity from `BackpackLayerModel#livingEntity`")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearEntityFromModel);
	}

	static {
		// Dummy to validate variable calls
		BackpackLayerModel.LAYER_MODEL.setLivingEntity(null);
	}

	private void clearEntityFromModel(UpdateableLevel.RenderEnginesUpdated event) {
		BackpackLayerModel.LAYER_MODEL.setLivingEntity(null);
	}
}
