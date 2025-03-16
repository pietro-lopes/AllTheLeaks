package dev.uncandango.alltheleaks.leaks.client.mods.beansbackpacks;

import com.beansgalaxy.backpacks.client.network.CommonAtClient;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;

@Issue(modId = "beansbackpacks", versionRange = "[2.0,)", description = "Clears `EnderStorage#MAP` map on clone/logout/level load")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearPlayerClone);
		gameBus.addListener(this::clearPlayerLogout);
		gameBus.addListener(this::clearPlayerOnLoadLevel);
	}

	static {
		// integrity check
		var dummy = CommonAtClient.getEnderStorage().MAP;
	}

	private void clearPlayerClone(ClientPlayerNetworkEvent.Clone event) {
		CommonAtClient.getEnderStorage().MAP.remove(event.getOldPlayer().getUUID());
	}

	private void clearPlayerOnLoadLevel(UpdateableLevel.RenderEnginesUpdated event) {
		CommonAtClient.getEnderStorage().MAP.clear();
	}

	private void clearPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
		if (event.getPlayer() == null) return;
		CommonAtClient.getEnderStorage().MAP.remove(event.getPlayer().getUUID());
	}
}
