package dev.uncandango.alltheleaks.leaks.client.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.common.NeoForge;

@Issue(modId = "minecraft", versionRange = "1.21.1", mixins = {"main.MinecraftMixin"})
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearEntities);
	}

	private void clearEntities(UpdateableLevel.RenderEnginesUpdated event) {
		Minecraft.getInstance().crosshairPickEntity = null;
		Minecraft.getInstance().hitResult = null;
//			Minecraft.getInstance().getEntityRenderDispatcher().crosshairPickEntity = null;
//			Minecraft.getInstance().getBlockEntityRenderDispatcher().cameraHitResult = null;
	}
}
