package dev.uncandango.alltheleaks.leaks.client.mods.mts;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import mcinterface1201.WrapperPlayer;
import mcinterface1201.WrapperWorld;
import minecrafttransportsimulator.baseclasses.EntityManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;

import java.lang.invoke.VarHandle;
import java.util.List;
import java.util.Map;

@Issue(modId = "mts", versionRange = "[24.0.0,)", mixins = "main.WrapperWorldMixin")
public class UntrackedIssue001 {
	private static final VarHandle MANAGERS;
	private static final VarHandle WORLD;
	private static final VarHandle WORLD_WRAPPERS;
	private static final VarHandle PLAYER_CLIENT_WRAPPERS;

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::onLevelUnload);
		gameBus.addListener(this::onPlayerClone);
	}

	private void onLevelUnload(UpdateableLevel.RenderEnginesUpdated event) {
		((List<EntityManager>)MANAGERS.get()).removeIf(m -> ((Level)WORLD.get(m.getWorld())).isClientSide());
		var map = ((Map<Level,WrapperWorld>)WORLD_WRAPPERS.get());
		map.keySet().removeIf(Level::isClientSide);
	}

	private void onPlayerClone(ClientPlayerNetworkEvent.Clone event){
		((Map<Player,Object>)PLAYER_CLIENT_WRAPPERS.get()).remove(event.getOldPlayer());
	}

	static {
		MANAGERS = ReflectionHelper.getFieldFromClass(EntityManager.class, "managers", List.class, true);
		WORLD = ReflectionHelper.getFieldFromClass(WrapperWorld.class, "world", Level.class, false);
		WORLD_WRAPPERS = ReflectionHelper.getFieldFromClass(WrapperWorld.class, "worldWrappers", Map.class, true);
		PLAYER_CLIENT_WRAPPERS = ReflectionHelper.getFieldFromClass(WrapperPlayer.class, "playerClientWrappers", Map.class, true);
	}

}
