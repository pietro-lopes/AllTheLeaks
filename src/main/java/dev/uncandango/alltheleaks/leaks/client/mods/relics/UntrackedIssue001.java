package dev.uncandango.alltheleaks.leaks.client.mods.relics;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import it.hurts.sskirillss.relics.client.handlers.DescriptionHandler;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;

@Issue(modId = "relics", versionRange = "[0.10.4,)")
public class UntrackedIssue001 {
	public static final VarHandle SLOT;
	public static final VarHandle TICKS_COUNT_OLD;
	public static final VarHandle TICKS_COUNT;

	static {
		SLOT = ReflectionHelper.getFieldFromClass(DescriptionHandler.class, "slot", Slot.class, true);
		TICKS_COUNT_OLD = ReflectionHelper.getFieldFromClass(DescriptionHandler.class, "ticksCountOld", int.class, true);
		TICKS_COUNT = ReflectionHelper.getFieldFromClass(DescriptionHandler.class, "ticksCount", int.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearHandlder);
		gameBus.addListener(this::clearHandlderOnClone);
	}

	private void clear() {
		SLOT.set((Object) null);
		TICKS_COUNT_OLD.set(0);
		TICKS_COUNT.set(0);
	}

	private void clearHandlder(LevelEvent.Unload event) {
		if (event.getLevel() != null && event.getLevel().isClientSide()) {
			clear();
		}
	}

	private void clearHandlderOnClone(ClientPlayerNetworkEvent.Clone event) {
		clear();
	}
}
