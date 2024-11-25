package dev.uncandango.alltheleaks.leaks.client.mods.pneumaticcraft;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.ArmorMainScreenAccessor;
import me.desht.pneumaticcraft.client.gui.pneumatic_armor.ArmorMainScreen;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;

@Issue(modId = "pneumaticcraft", versionRange = "[6.0.15,)", mixins = "accessor.ArmorMainScreenAccessor",
description = "Clears `ArmorMainScreen#upgradeOptions` on client player clone")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearOptionsPage);
		gameBus.addListener(this::clearOptionsPageOnLogout);
	}

	private void clearOptionsPage(ClientPlayerNetworkEvent.Clone event) {
		if (ArmorMainScreen.getInstance() instanceof ArmorMainScreenAccessor accessor) {
			accessor.getUpgradeOptions().clear();
		}
	}

	private void clearOptionsPageOnLogout(ClientPlayerNetworkEvent.LoggingOut event) {
		if (ArmorMainScreen.getInstance() instanceof ArmorMainScreenAccessor accessor) {
			accessor.getUpgradeOptions().clear();
		}
	}
}
