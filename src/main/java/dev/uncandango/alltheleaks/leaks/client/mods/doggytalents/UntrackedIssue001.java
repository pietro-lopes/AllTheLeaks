package dev.uncandango.alltheleaks.leaks.client.mods.doggytalents;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import doggytalents.client.screen.widget.DogInventoryButton;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.invoke.VarHandle;

@Issue(modId = "doggytalents", versionRange = "[1.18.49,)")
public class UntrackedIssue001 {
	public static final VarHandle INVENTORY_BUTTON;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearInvButton);
		gameBus.addListener(this::clearInvButtonOnLevelUpdate);
	}

	private void clearInvButtonOnLevelUpdate(UpdateableLevel.RenderEnginesUpdated event) {
		INVENTORY_BUTTON.set((Object) null);
	}

	static {
		INVENTORY_BUTTON = ReflectionHelper.getFieldFromClass(DogInventoryButton.class, "inventoryButton", DogInventoryButton.class, true);
	}

	private void clearInvButton(ClientPlayerNetworkEvent.Clone event) {
		INVENTORY_BUTTON.set((Object) null);
	}
}
