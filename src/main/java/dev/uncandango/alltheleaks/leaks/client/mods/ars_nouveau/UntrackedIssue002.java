package dev.uncandango.alltheleaks.leaks.client.mods.ars_nouveau;

import com.hollingsworth.arsnouveau.client.events.ClientEvents;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;

@Issue(modId = "ars_nouveau", versionRange = "[5.8.2,)")
public class UntrackedIssue002 {
	public static final VarHandle SLOT_UNDER_MOUSE;

	static {
		SLOT_UNDER_MOUSE = ReflectionHelper.getFieldFromClass(ClientEvents.class, "slotUnderMouse", Slot.class, true);
	}

	public UntrackedIssue002() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearSlot);
		gameBus.addListener(this::clearSlotOnLevelUnload);
	}

	private void clearSlotOnLevelUnload(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
			SLOT_UNDER_MOUSE.set((Object) null);
		}
	}

	private void clearSlot(ClientPlayerNetworkEvent.Clone event) {
		SLOT_UNDER_MOUSE.set((Object) null);
	}
}

