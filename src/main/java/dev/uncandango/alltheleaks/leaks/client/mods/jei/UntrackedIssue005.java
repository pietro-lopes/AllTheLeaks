package dev.uncandango.alltheleaks.leaks.client.mods.jei;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.main.accessor.LookupHistoryJsonConfigAccessor;
import dev.uncandango.alltheleaks.plugins.ATLJeiPlugin;
import net.neoforged.neoforge.common.NeoForge;

@Issue(modId = "jei", versionRange = "[19.25.0.318,19.27.0.336)", mixins = {"main.accessor.LookupHistoryJsonConfigAccessor"})
public class UntrackedIssue005 {
	public UntrackedIssue005() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::onRuntimeUnavailable);
	}

	private void onRuntimeUnavailable(ATLJeiPlugin.RuntimeUnavailableEvent event) {
		LookupHistoryJsonConfigAccessor.atl$setBookmarkCodec(null);
	}
}
