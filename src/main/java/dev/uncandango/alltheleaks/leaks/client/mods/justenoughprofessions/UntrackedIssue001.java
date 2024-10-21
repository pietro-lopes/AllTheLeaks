package dev.uncandango.alltheleaks.leaks.client.mods.justenoughprofessions;

import com.mrbysco.justenoughprofessions.VillagerCache;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;

@Issue(modId = "justenoughprofessions", versionRange = "[4.0.3,)")
public class UntrackedIssue001 {
	public static final VarHandle CACHED_VILLAGER;

	static {
		CACHED_VILLAGER = ReflectionHelper.getFieldFromClass(VillagerCache.class, "cachedVillager", Villager.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearCachedVillager);
	}

	private void clearCachedVillager(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
			CACHED_VILLAGER.set((Object) null);
		}
	}
}
