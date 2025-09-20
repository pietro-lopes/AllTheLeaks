package dev.uncandango.alltheleaks.leaks.client.mods.ftbchunks;

import dev.ftb.mods.ftbchunks.client.FTBChunksClient;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.invoke.VarHandle;
import java.util.List;

@Issue(modId = "ftbchunks", versionRange = "*")
public class UntrackedIssue001 {
	public static final VarHandle MAP_ICONS;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearMapIcons);
	}

	static {
		var dummy = FTBChunksClient.INSTANCE;
		MAP_ICONS = ReflectionHelper.getFieldFromClass(FTBChunksClient.class, "mapIcons", List.class, false);
	}

	private void clearMapIcons(UpdateableLevel.RenderEnginesUpdated event) {
		((List)MAP_ICONS.get(FTBChunksClient.INSTANCE)).clear();
	}
}
