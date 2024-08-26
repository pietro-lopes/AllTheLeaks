package dev.uncandango.alltheleaks.leaks.common.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@Issue(modId = "minecraft", versionRange = "[1.21,1.21.1]")
public class UntrackedIssue002 {
	public UntrackedIssue002() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearDamageSource);
	}

	private void clearDamageSource(EntityTickEvent.Post event) {
		if (event.getEntity().tickCount % 41 == 0 && event.getEntity() instanceof LivingEntity living) {
			living.getLastDamageSource();
		}
	}
}
