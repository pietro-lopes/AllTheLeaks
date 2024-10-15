package dev.uncandango.alltheleaks.leaks.client.mods.pneumaticcraft;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.CommonArmorHandlerAccessor;
import net.minecraft.client.player.RemotePlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;

@Issue(modId = "pneumaticcraft", versionRange = "[8.0.3,)", mixins = "accessor.CommonArmorHandlerAccessor")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearRemotePlayer);
	}

	private void clearRemotePlayer(EntityLeaveLevelEvent event) {
		if (event.getEntity() instanceof RemotePlayer player) {
			CommonArmorHandlerAccessor.invokeClearHandlerForPlayer(player);
		}
	}
}
