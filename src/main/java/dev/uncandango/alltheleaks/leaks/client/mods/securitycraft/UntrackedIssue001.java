package dev.uncandango.alltheleaks.leaks.client.mods.securitycraft;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.geforcemods.securitycraft.entity.camera.CameraController;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.invoke.VarHandle;

@Issue(modId = "securitycraft", versionRange = "[1.9.10-beta9,)")
public class UntrackedIssue001 {
	private static final VarHandle CAMERA_STORAGE;

	static {
		Class<?> storageClass = ReflectionHelper.getPrivateClass(ClientChunkCache.class, "net.minecraft.client.multiplayer.ClientChunkCache$Storage");
		CAMERA_STORAGE = ReflectionHelper.getFieldFromClass(CameraController.class, "cameraStorage", storageClass, true);
	}

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearCameraStorage);
	}

	private void clearCameraStorage(ClientPlayerNetworkEvent.LoggingOut event) {
//		CAMERA_STORAGE.set((Object) null);
	}
}
