package dev.uncandango.alltheleaks.leaks.client.mods.easy_piglins;

import de.maxhenkel.easypiglins.corelib.CachedMap;
import de.maxhenkel.easypiglins.items.ModItems;
import de.maxhenkel.easypiglins.items.PiglinItem;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;

import java.lang.invoke.VarHandle;

@Issue(modId = "easy_piglins", versionRange = "*")
public class UntrackedIssue001 {
	public static final VarHandle CACHED_PIGLINS;

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearCacheOnLevelUpdate);
	}

	static {
		var dummy = ModItems.PIGLIN.get();
		CACHED_PIGLINS = ReflectionHelper.getFieldFromClass(PiglinItem.class, "cachedPiglins", CachedMap.class, false);
	}

	private void clearCacheOnLevelUpdate(UpdateableLevel.RenderEnginesUpdated event) {
		((CachedMap)CACHED_PIGLINS.get(ModItems.PIGLIN.get())).clear();
	}
}
