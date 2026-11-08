package dev.uncandango.alltheleaks.events.common;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.commands.ATLCommands;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.MemoryMonitor;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.PlayerReviveFix;
import dev.uncandango.alltheleaks.mixin.Trackable;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

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
	public static void onServerStart(ServerStartedEvent event){
		if (AllTheLeaks.INDEV) {
			AllTheLeaks.LOGGER.info("Logging blockstate counts...");
			Map<String, Long> blockStateCount = new HashMap<>();
			for (var blockState : Block.BLOCK_STATE_REGISTRY) {
				var modId = ForgeRegistries.BLOCKS.getKey(blockState.getBlock()).getNamespace();
				blockStateCount.compute(modId, (k, v) -> v == null ? 1L : v + 1L);
			}
			var list = blockStateCount.entrySet().stream().sorted(Map.Entry.comparingByValue()).toList();
			list.forEach(entry -> {
				AllTheLeaks.LOGGER.info("Mod \"{}\" has {} blockstates.", entry.getKey(), entry.getValue());
			});
		}
	}

	@SubscribeEvent
	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		Trackable.startTracking(event.getEntity());
		MemoryMonitor.EventStatistics.SERVER_PLAYER_LOGOUT.increment();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPlayerCloneHighest(PlayerEvent.Clone event){
		PlayerReviveFix.setIsBeingCloned(true);
//		if (event.getOriginal().getRemovalReason() == null) {
//			if (ModList.get().isLoaded("playerrevive")) return; // we skip if this mod is loaded
//			var error = new ATLIllegalState(ATLIllegalState.TYPE.NO_REMOVAL_REASON,"Cannot have null removal reason on Clone event!");
//			AllTheLeaks.LOGGER.error(error.getMessage());
//		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerCloneLowest(PlayerEvent.Clone event){
//		if (event.getOriginal().getRemovalReason() == null) {
//			if (ModList.get().isLoaded("playerrevive")) return; // we skip if this mod is loaded
//			var error = new ATLIllegalState(ATLIllegalState.TYPE.NO_REMOVAL_REASON,"Cannot have null removal reason on Clone event!");
//			AllTheLeaks.LOGGER.error(error.getMessage());
//		}
		event.getOriginal().invalidateCaps();
		PlayerReviveFix.setIsBeingCloned(false);
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

	@SubscribeEvent
	public static void registerCommonCommands(RegisterCommandsEvent event) {
		ATLCommands.registerCommonCommands(event.getDispatcher(), event.getBuildContext());
	}
}
