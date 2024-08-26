package dev.uncandango.alltheleaks.leaks.client.mods.ftblibrary;

import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.BaseScreenAccessor;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

@Issue(modId = "ftblibrary", versionRange = "[2101.1.1,)", mixins = "accessor.BaseScreenAccessor")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearPreviousFromClone);
		gameBus.addListener(this::clearPreviousFromUnload);
	}

	private void clearPreviousFromClone(ClientPlayerNetworkEvent.Clone event) {
		if (GuiHelper.BLANK_GUI instanceof BaseScreenAccessor accessor) {
			accessor.atl$setPrevScreen(null);
		}
	}

	private void clearPreviousFromUnload(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
			if (GuiHelper.BLANK_GUI instanceof BaseScreenAccessor accessor) {
				accessor.atl$setPrevScreen(null);
			}
		}
	}
}
