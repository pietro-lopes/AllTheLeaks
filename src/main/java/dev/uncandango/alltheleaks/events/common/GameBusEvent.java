package dev.uncandango.alltheleaks.events.common;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.exceptions.ATLIllegalState;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.MemoryMonitor;
import dev.uncandango.alltheleaks.mixin.Trackable;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AllTheLeaks.MOD_ID)
public class GameBusEvent {
	@SubscribeEvent
	public static void onShutdownServer(ServerStoppingEvent event) {
		Trackable.startTracking(event.getServer());
	}

	@SubscribeEvent
	public static void onClosedServer(ServerStoppedEvent event) {
		MemoryMonitor.runExplicitGc();
		Trackable.clearNullReferences();
		MemoryMonitor.EventStatistics.SERVER_STOP.increment();
	}

	@SubscribeEvent
	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		Trackable.startTracking(event.getEntity());
		MemoryMonitor.EventStatistics.SERVER_PLAYER_LOGOUT.increment();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerCloneLowest(PlayerEvent.Clone event){
		if (event.getOriginal().getRemovalReason() == null) {
			if (ModList.get().isLoaded("playerrevive")) return; // we skip if this mod is loaded
			var error = new ATLIllegalState(ATLIllegalState.TYPE.NO_REMOVAL_REASON,"Cannot have null removal reason on Clone event!");
			AllTheLeaks.LOGGER.error(error.getMessage());
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event){
		Trackable.startTracking(event.getOriginal());
		MemoryMonitor.EventStatistics.SERVER_PLAYER_CLONE.increment();
	}

	@SubscribeEvent
	public static void onUnloadChunk(ChunkEvent.Unload event) {
		Trackable.startTracking(event.getChunk());
	}

	@SubscribeEvent
	public static void onUnloadLevel(LevelEvent.Unload event) {
		Trackable.startTracking(event.getLevel());
	}
}
