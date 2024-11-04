package dev.uncandango.alltheleaks.leaks.client.mods.ftblibrary;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.BaseScreenAccessor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;

import static dev.ftb.mods.ftblibrary.ui.GuiHelper.BLANK_GUI;

@Issue(modId = "ftblibrary", versionRange = "[2001.2.2,)", mixins = "accessor.BaseScreenAccessor",
description = "Clears `BLANK_GUI#prevScreen` on level unload")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearOnLevelUnload);
	}

	private void clearOnLevelUnload(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
			if (BLANK_GUI instanceof BaseScreenAccessor accessor) {
				accessor.atl$setprevScreen(null);
			}
		}
	}
}
