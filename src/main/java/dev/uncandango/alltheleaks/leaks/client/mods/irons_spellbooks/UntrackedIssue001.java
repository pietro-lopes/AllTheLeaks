package dev.uncandango.alltheleaks.leaks.client.mods.irons_spellbooks;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.gui.overlays.SpellBarOverlay;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;

import java.lang.invoke.VarHandle;

@Issue(modId = "irons_spellbooks", versionRange = "[1.20.1-3.4.0,)",
	description = "Clears `SpellBarOverlay#lastSelection` and `ClientMagicData#spellSelectionManager` on client clone")
public class UntrackedIssue001 {
	public static final VarHandle LAST_SELECTION;
	public static final VarHandle SPELL_SELECTION_MANAGER;
	static {
		LAST_SELECTION = ReflectionHelper.getFieldFromClass(SpellBarOverlay.class, "lastSelection", SpellSelectionManager.class, true);
		SPELL_SELECTION_MANAGER = ReflectionHelper.getFieldFromClass(ClientMagicData.class, "spellSelectionManager", SpellSelectionManager.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearLastSelection);
	}

	private void clearLastSelection(ClientPlayerNetworkEvent.Clone event) {
		LAST_SELECTION.set((Object)null);
		SPELL_SELECTION_MANAGER.set((Object)null);
	}
}
