package dev.uncandango.alltheleaks.leaks.client.mods.mffs;

import dev.su5ed.mffs.util.FrequencyGrid;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.invoke.VarHandle;

@Issue(modId = "mffs", versionRange = "[5.4.17,)")
public class UntrackedIssue001 {
	public static final VarHandle CLIENT_INSTANCE;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearOnLevelUpdate);
	}

	static {
		CLIENT_INSTANCE = ReflectionHelper.getFieldFromClass(FrequencyGrid.class, "CLIENT_INSTANCE", FrequencyGrid.class, true);
	}

	private void clearOnLevelUpdate(UpdateableLevel.RenderEnginesUpdated event) {
		CLIENT_INSTANCE.set(new FrequencyGrid());
	}
}
