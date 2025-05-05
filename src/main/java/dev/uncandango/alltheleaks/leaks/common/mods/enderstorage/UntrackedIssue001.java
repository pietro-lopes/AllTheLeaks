package dev.uncandango.alltheleaks.leaks.common.mods.enderstorage;

import codechicken.enderstorage.network.TankSynchroniser;
import codechicken.enderstorage.network.TankSynchroniser.PlayerItemTankCache;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.lang.invoke.VarHandle;
import java.util.Map;
import java.util.UUID;

@Issue(modId = "enderstorage", versionRange = "[2.13.0.191,)")
public class UntrackedIssue001 {
	public static final VarHandle PLAYER_ITEM_TANK_STATES;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::updateTankCache);
	}

	static {
		PLAYER_ITEM_TANK_STATES = ReflectionHelper.getFieldFromClass(TankSynchroniser.class, "playerItemTankStates", Map.class, true);
	}

	private void updateTankCache(PlayerEvent.Clone event) {
		((Map<UUID, PlayerItemTankCache>)PLAYER_ITEM_TANK_STATES.get()).put(event.getEntity().getUUID(), new PlayerItemTankCache((ServerPlayer) event.getEntity()));
	}
}
