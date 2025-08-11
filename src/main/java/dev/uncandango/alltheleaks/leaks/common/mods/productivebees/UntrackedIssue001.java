package dev.uncandango.alltheleaks.leaks.common.mods.productivebees;

import cy.jdkdigital.productivebees.common.block.entity.AmberBlockEntity;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "productivebees", versionRange = "[1.20.1-12.6.0,)")
public class UntrackedIssue001 {
	private static final VarHandle CACHED_ENTITIES;

	static {
		CACHED_ENTITIES = ReflectionHelper.getFieldFromClass(AmberBlockEntity.class, "cachedEntities", Map.class, true);
	}
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearEntitiesOnLevelUnload);
	}

	private void clearEntitiesOnLevelUnload(LevelEvent.Unload event) {
		if (!event.getLevel().isClientSide()) {
			((Map<Integer, Entity>) CACHED_ENTITIES.get()).entrySet().removeIf(entry -> entry.getValue().level() == event.getLevel());
		}
	}
}
