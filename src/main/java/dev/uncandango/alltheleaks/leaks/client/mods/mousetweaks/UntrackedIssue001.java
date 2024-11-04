package dev.uncandango.alltheleaks.leaks.client.mods.mousetweaks;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import yalter.mousetweaks.IGuiScreenHandler;
import yalter.mousetweaks.Main;

import java.lang.invoke.VarHandle;

@Issue(modId = "mousetweaks", versionRange = "[2.25.1,)",
	description = "Clears `Main#openScreen/handler/oldSelectedSlot` on client player clone")
public class UntrackedIssue001 {
	public static final VarHandle OPEN_SCREEN;
	public static final VarHandle HANDLER;
	public static final VarHandle OLD_SELECTED_SLOT;

	static {
		OPEN_SCREEN = ReflectionHelper.getFieldFromClass(Main.class, "openScreen", Screen.class, true);
		HANDLER = ReflectionHelper.getFieldFromClass(Main.class, "handler", IGuiScreenHandler.class, true);
		OLD_SELECTED_SLOT = ReflectionHelper.getFieldFromClass(Main.class, "oldSelectedSlot", Slot.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearLastScreenOpen);
	}

	private void clearLastScreenOpen(ClientPlayerNetworkEvent.Clone event) {
		OPEN_SCREEN.set((Object) null);
		HANDLER.set((Object) null);
		OLD_SELECTED_SLOT.set((Object) null);
	}
}
