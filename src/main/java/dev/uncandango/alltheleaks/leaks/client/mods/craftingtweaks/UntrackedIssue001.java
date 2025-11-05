package dev.uncandango.alltheleaks.leaks.client.mods.craftingtweaks;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.blay09.mods.craftingtweaks.client.CraftingTweaksClient;
import net.minecraft.client.gui.components.AbstractWidget;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.invoke.VarHandle;

@Issue(modId = "craftingtweaks", versionRange = "[21.1.6,)")
public class UntrackedIssue001 {
	public static final VarHandle UNPLEASANT_BUTTON;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearWidgetOnLogout);
		gameBus.addListener(this::clearWidgetOnClone);
	}

	static {
		UNPLEASANT_BUTTON = ReflectionHelper.getFieldFromClass(CraftingTweaksClient.class, "unpleasantButton", AbstractWidget.class, true);
	}

	private void clearWidgetOnClone(ClientPlayerNetworkEvent.Clone event) {
		UNPLEASANT_BUTTON.set((Object)null);
	}

	private void clearWidgetOnLogout(ClientPlayerNetworkEvent.LoggingOut event) {
		UNPLEASANT_BUTTON.set((Object)null);
	}
}
