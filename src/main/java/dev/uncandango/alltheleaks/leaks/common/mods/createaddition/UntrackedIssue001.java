package dev.uncandango.alltheleaks.leaks.common.mods.createaddition;

import com.mrh0.createaddition.energy.network.EnergyNetworkManager;
import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;

@Issue(modId = "createaddition", versionRange = "[1.20.1-1.0.0b,1.20.1-1.2.3]",
	description = "Clears level from `EnergyNetworkManager#instances` on level unload")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearLevelFromNetwork);
	}

	private void clearLevelFromNetwork(LevelEvent.Unload event) {
		if (!event.getLevel().isClientSide()) {
			EnergyNetworkManager.instances.remove(event.getLevel());
		}
	}
}
