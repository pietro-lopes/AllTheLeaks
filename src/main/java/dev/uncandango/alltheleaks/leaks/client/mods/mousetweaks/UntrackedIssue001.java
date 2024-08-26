package dev.uncandango.alltheleaks.leaks.client.mods.mousetweaks;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import yalter.mousetweaks.IGuiScreenHandler;
import yalter.mousetweaks.Main;

import java.lang.invoke.VarHandle;

@Issue(modId = "mousetweaks", versionRange = "[2.26.1,)")
public class UntrackedIssue001 {
	private static final VarHandle OPEN_SCREEN;
	private static final VarHandle OLD_SELECTED_SLOT;
	private static final VarHandle HANDLER;

	static {
		OPEN_SCREEN = ReflectionHelper.getFieldFromClass(Main.class, "openScreen", Screen.class, true);
		OLD_SELECTED_SLOT = ReflectionHelper.getFieldFromClass(Main.class, "oldSelectedSlot", Slot.class, true);
		HANDLER = ReflectionHelper.getFieldFromClass(Main.class, "handler", IGuiScreenHandler.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearScreenFromMod);
	}

	private void clearScreenFromMod(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
			OPEN_SCREEN.set((Object) null);
			OLD_SELECTED_SLOT.set((Object) null);
			HANDLER.set((Object) null);
		}
	}
}
