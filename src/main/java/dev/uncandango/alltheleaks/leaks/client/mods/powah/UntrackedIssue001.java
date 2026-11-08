package dev.uncandango.alltheleaks.leaks.client.mods.powah;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import owmii.powah.block.cable.CableNet;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "powah", versionRange = "[5.0.11,)")
public class UntrackedIssue001 {
	private static final VarHandle LOADED_CABLES;

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearOnLevelUnload);
	}

	static {
		LOADED_CABLES = ReflectionHelper.getFieldFromClass(CableNet.class, "loadedCables", Map.class, true);
	}

	private void clearOnLevelUnload(LevelEvent.Unload event) {
		if (event.getLevel() instanceof ClientLevel level) {
			((Map<Level,Object>)LOADED_CABLES.get()).remove(level);
		}
	}
}
