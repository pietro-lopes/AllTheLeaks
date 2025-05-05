package dev.uncandango.alltheleaks.leaks.client.mods.ars_nouveau;

import com.hollingsworth.arsnouveau.client.gui.GuiEntityInfoHUD;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;

@Issue(modId = "ars_nouveau", versionRange = "[5.0.12,)")
public class UntrackedIssue001 {
	public static final VarHandle LAST_HOVERED;

	static {
		LAST_HOVERED = ReflectionHelper.getFieldFromClass(GuiEntityInfoHUD.class, "lastHovered", Object.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearHovered);
		gameBus.addListener(this::clearHoveredOnClone);
	}

	private void clearHovered(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
			LAST_HOVERED.set((Object) null);
		}
	}

	private void clearHoveredOnClone(ClientPlayerNetworkEvent.Clone event) {
		LAST_HOVERED.set((Object) null);
	}
}

