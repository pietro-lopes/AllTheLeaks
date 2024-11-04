package dev.uncandango.alltheleaks.leaks.common.mods.create;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.ExtendoGripItemAccessor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

@Issue(modId = "create", versionRange = "[0.5.1.c,)", mixins = "accessor.ExtendoGripItemAccessor",
description = "Clears last damage on clone from `ExtendoGripItem#lastActiveDamageSource`")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearLastDamageOnClone);
	}

	private void clearLastDamageOnClone(PlayerEvent.Clone event) {
		ExtendoGripItemAccessor.setLastActiveDamageSource(null);
	}
}
