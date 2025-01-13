package dev.uncandango.alltheleaks.leaks.client.mods.justdirethings;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.player.AbstractClientPlayer;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.invoke.VarHandle;

@Issue(modId = "justdirethings", versionRange = "[1.5.1,)")
public class UntrackedIssue001 {
	public static final VarHandle MOCK_PLAYER;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearPlayerOnRenderEngineUpdate);
	}

	static {
		var clazz = ReflectionHelper.getClass("com.direwolf20.justdirethings.client.blockentityrenders.InventoryHolderBER");
		MOCK_PLAYER = ReflectionHelper.getFieldFromClass(clazz, "mockPlayer", AbstractClientPlayer.class, true);
	}

	private void clearPlayerOnRenderEngineUpdate(UpdateableLevel.RenderEnginesUpdated event) {
		MOCK_PLAYER.set((Object) null);
	}
}
