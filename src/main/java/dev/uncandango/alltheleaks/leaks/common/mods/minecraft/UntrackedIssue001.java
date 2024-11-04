package dev.uncandango.alltheleaks.leaks.common.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

@Issue(modId = "minecraft", versionRange = "1.20.1",
	description = "Adds a check of last damage to clear removed entities from it")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearLastDamageSource);
	}

	private void clearLastDamageSource(LivingEvent.LivingTickEvent event) {
		if (event.getEntity().tickCount % 41 == 0) {
			event.getEntity().getLastDamageSource();
		}
	}
}
