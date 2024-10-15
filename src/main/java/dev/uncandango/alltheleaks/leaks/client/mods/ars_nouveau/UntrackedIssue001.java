package dev.uncandango.alltheleaks.leaks.client.mods.ars_nouveau;

import com.hollingsworth.arsnouveau.client.gui.GuiEntityInfoHUD;
import com.hollingsworth.arsnouveau.common.camera.CameraController;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;

@Issue(modId = "ars_nouveau", versionRange = "[5.0.12,)")
public class UntrackedIssue001 {
	public static final VarHandle CAMERA_STORAGE;
	public static final VarHandle LAST_HOVERED;

	static {
		Class<?> storageClass = ReflectionHelper.getPrivateClass(ClientChunkCache.class, "net.minecraft.client.multiplayer.ClientChunkCache$Storage");
		CAMERA_STORAGE = ReflectionHelper.getFieldFromClass(CameraController.class, "cameraStorage", storageClass, true);
		LAST_HOVERED = ReflectionHelper.getFieldFromClass(GuiEntityInfoHUD.class, "lastHovered", Object.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearCameraStorage);
	}

	private void clearCameraStorage(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
//			CAMERA_STORAGE.set((Object) null);
			LAST_HOVERED.set((Object) null);
//			GuiEntityInfoHUD.lastHovered = null;
		}
	}
}

