package dev.uncandango.alltheleaks.leaks.client.mods.trophymanager;

import cy.jdkdigital.trophymanager.common.blockentity.TrophyBlockEntity;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "trophymanager", versionRange = "[1.21.1-2.1.8,1.21.1-2.2.4]")
public class UntrackedIssue001 {
	public static final VarHandle CACHED_ENTITIES;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearTrophyManagerCache);
	}

	static {
		CACHED_ENTITIES = ReflectionHelper.getFieldFromClass(TrophyBlockEntity.class, "cachedEntities", Map.class, true);
	}

	private void clearTrophyManagerCache(LevelEvent.Unload event) {
		((Map<Integer, Entity>)CACHED_ENTITIES.get()).entrySet().removeIf(entry -> entry.getValue() != null && entry.getValue().level() == event.getLevel());
	}
}
