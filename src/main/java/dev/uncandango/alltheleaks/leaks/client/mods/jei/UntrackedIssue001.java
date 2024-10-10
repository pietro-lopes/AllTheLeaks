package dev.uncandango.alltheleaks.leaks.client.mods.jei;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.RecipeTransferManagerAccessor;
import mezz.jei.common.Internal;
import mezz.jei.library.runtime.JeiRuntime;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;

@Issue(modId = "jei", versionRange = "[19.19.5.232,)", mixins = "accessor.RecipeTransferManagerAccessor")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearCachedInventories);
	}

	private void clearCachedInventories(ClientPlayerNetworkEvent.Clone event) {
		try {
			var runTime = (JeiRuntime) Internal.getJeiRuntime();
			if (runTime.getRecipeTransferManager() instanceof RecipeTransferManagerAccessor accessor) {
				accessor.getUnsupportedContainers().clear();
			}
		} catch (Exception ignored) {
		}
	}
}
