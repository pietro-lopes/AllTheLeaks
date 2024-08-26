package dev.uncandango.alltheleaks.leaks.client.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

@Issue(modId = "minecraft", versionRange = "[1.21,1.21.1]")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearEntities);
	}

	private void clearEntities(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
			Minecraft.getInstance().crosshairPickEntity = null;
			Minecraft.getInstance().hitResult = null;
//			Minecraft.getInstance().getEntityRenderDispatcher().crosshairPickEntity = null;
//			Minecraft.getInstance().getBlockEntityRenderDispatcher().cameraHitResult = null;
		}
	}
}
