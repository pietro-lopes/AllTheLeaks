package dev.uncandango.alltheleaks.leaks.client.mods.difficultylock;

import com.natamus.difficultylock_common_forge.util.Util;
import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppedEvent;

@Issue(modId = "difficultylock", versionRange = "[4.1,)", description = "Clears `Util` variables on server stop")
public class UntrackedIssue001 {

	static {
		// Dummy values to validate variable calls
		CycleButton dummy = null;
		var dummy2 = Util.buttonUpdatesLeft;
		dummy = Util.gameModeButton;
		dummy = Util.allowCheatsButton;
		dummy = Util.difficultyButton;
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearGameButtons);
	}

	private void clearGameButtons(ServerStoppedEvent event) {
		Util.buttonUpdatesLeft = 3;
		Util.gameModeButton = null;
		Util.allowCheatsButton = null;
		Util.difficultyButton = null;
	}
}
