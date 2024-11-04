package dev.uncandango.alltheleaks.leaks.client.mods.easy_villagers;

import de.maxhenkel.easyvillagers.ItemTileEntityCache;
import de.maxhenkel.easyvillagers.corelib.CachedMap;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.mixin.core.accessor.VillagerItemAccessor;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;

import java.lang.invoke.VarHandle;

import static de.maxhenkel.easyvillagers.items.ModItems.VILLAGER;

@Issue(modId = "easy_villagers", versionRange = "[1.20.1-1.1.4,)", mixins = {"accessor.VillagerItemAccessor", "main.ConverterRendererMixin", "main.IronFarmRendererMixin"},
	description = "Clears `ItemTileEntityCache#CACHE` and `VillagerItem#cachedVillagers` cache on client level update")
public class UntrackedIssue001 {
	public static final VarHandle CACHE;

	static {
		CACHE = ReflectionHelper.getFieldFromClass(ItemTileEntityCache.class, "CACHE", CachedMap.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearOnLevelUnload);
	}

	@SuppressWarnings("rawtypes")
	private void clearOnLevelUnload(UpdateableLevel.RenderEnginesUpdated event) {
		((CachedMap) CACHE.get()).clear();
		if (VILLAGER.get() instanceof VillagerItemAccessor accessor) {
			accessor.getCachedVillagers().clear();
		}
	}
}
