package dev.uncandango.alltheleaks.leaks.client.mods.clientsort;

import dev.terminalmc.clientsort.client.gui.TriggerButtonManager;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;

import java.lang.invoke.VarHandle;
import java.util.LinkedHashSet;

@Issue(modId = "clientsort", versionRange = "*", description = "Clear TriggerButtonManager#screen and buttons on clone and logout")
public class UntrackedIssue001 {
	public static final VarHandle SCREEN;
	public static final VarHandle CONTAINER_BUTTONS;
	public static final VarHandle VISIBLE_CONTAINER_BUTTONS;
	public static final VarHandle PLAYER_BUTTONS;
	public static final VarHandle VISIBLE_PLAYER_BUTTONS;

	static {
		SCREEN = ReflectionHelper.getFieldFromClass(TriggerButtonManager.class, "screen", AbstractContainerScreen.class, true);
		CONTAINER_BUTTONS = ReflectionHelper.getFieldFromClass(TriggerButtonManager.class, "containerButtons", LinkedHashSet.class, true);
		VISIBLE_CONTAINER_BUTTONS = ReflectionHelper.getFieldFromClass(TriggerButtonManager.class, "visibleContainerButtons", LinkedHashSet.class, true);
		PLAYER_BUTTONS = ReflectionHelper.getFieldFromClass(TriggerButtonManager.class, "playerButtons", LinkedHashSet.class, true);
		VISIBLE_PLAYER_BUTTONS = ReflectionHelper.getFieldFromClass(TriggerButtonManager.class, "visiblePlayerButtons", LinkedHashSet.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearOnClone);
		gameBus.addListener(this::clearOnLoggout);
	}

	private void clearOnLoggout(ClientPlayerNetworkEvent.LoggingOut event) {
		clearAll();
	}


	private void clearOnClone(ClientPlayerNetworkEvent.Clone event) {
		clearAll();
	}

	private void clearAll() {
		SCREEN.set((Object) null);
		((LinkedHashSet) CONTAINER_BUTTONS.get()).clear();
		((LinkedHashSet) VISIBLE_CONTAINER_BUTTONS.get()).clear();
		((LinkedHashSet) PLAYER_BUTTONS.get()).clear();
		((LinkedHashSet) VISIBLE_PLAYER_BUTTONS.get()).clear();
	}
}
