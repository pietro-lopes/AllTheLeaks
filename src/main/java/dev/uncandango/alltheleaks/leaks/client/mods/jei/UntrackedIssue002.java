package dev.uncandango.alltheleaks.leaks.client.mods.jei;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.BookmarkJsonConfigAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.TypedIngredientCodecsAccessor;
import dev.uncandango.alltheleaks.plugins.ATLJeiPlugin;
import net.neoforged.neoforge.common.NeoForge;

@Issue(modId = "jei", versionRange = "[19.16.4.168,)", mixins = {"accessor.BookmarkJsonConfigAccessor","accessor.TypedIngredientCodecsAccessor"})
public class UntrackedIssue002 {
	public UntrackedIssue002() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::onRuntimeUnavailable);
	}

	private void onRuntimeUnavailable(ATLJeiPlugin.RuntimeUnavailableEvent event) {
		BookmarkJsonConfigAccessor.atl$setBookmarkCodec(null);
		TypedIngredientCodecsAccessor.atl$setIngredientCodec(null);
		TypedIngredientCodecsAccessor.atl$setIngredientTypeCodec(null);
		TypedIngredientCodecsAccessor.atl$getCodecMapCache().clear();
	}
}
