package dev.uncandango.alltheleaks.leaks.common.mods.mts;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import mcinterface1201.WrapperEntity;
import mcinterface1201.WrapperPlayer;
import mcinterface1201.WrapperWorld;
import minecrafttransportsimulator.baseclasses.EntityManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;
import java.util.List;
import java.util.Map;

@Issue(modId = "mts", versionRange = "[24.0.0,)", mixins = {"main.WrapperWorldMixin","main.BuilderEntityRenderForwarderMixin"})
public class UntrackedIssue001 {
	private static final VarHandle MANAGERS;
	private static final VarHandle WORLD;
	private static final VarHandle PLAYER_SERVER_WRAPPERS;
	private static final VarHandle ENTITY_SERVER_WRAPPERS;

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::onLevelUnload);
		gameBus.addListener(this::onPlayerClone);
	}

	private void onLevelUnload(LevelEvent.Unload event) {
		if (event.getLevel() instanceof ServerLevel level) {
			((List<EntityManager>)MANAGERS.get()).removeIf(m -> WORLD.get(m.getWorld()) == level);
			((Map<Entity,Object>)ENTITY_SERVER_WRAPPERS.get()).keySet().removeIf(ent -> !ent.isAlive());
		}
	}

	private void onPlayerClone(PlayerEvent.Clone event){
		((Map<Player,Object>)PLAYER_SERVER_WRAPPERS.get()).remove(event.getOriginal());
	}

	static {
		MANAGERS = ReflectionHelper.getFieldFromClass(EntityManager.class, "managers", List.class, true);
		WORLD = ReflectionHelper.getFieldFromClass(WrapperWorld.class, "world", Level.class, false);
		PLAYER_SERVER_WRAPPERS = ReflectionHelper.getFieldFromClass(WrapperPlayer.class, "playerServerWrappers", Map.class, true);
		ENTITY_SERVER_WRAPPERS = ReflectionHelper.getFieldFromClass(WrapperEntity.class, "entityServerWrappers", Map.class, true);
	}

}
