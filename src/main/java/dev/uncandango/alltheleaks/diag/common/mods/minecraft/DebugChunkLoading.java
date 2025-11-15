package dev.uncandango.alltheleaks.diag.common.mods.minecraft;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.ChunkTicketLevelUpdatedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Issue(modId = "minecraft", issueId = "Debug ChunkLoading", versionRange = "[1.21.1]", mixins = {"debug.ChunkStatusTasksMixin", "debug.LevelChunkMixin", "debug.accessor.LevelAccessor", "debug.ChunkMapMixin", "debug.ServerLevelMixin", "debug.DistanceManagerMixin", "debug.accessor.ChunkMapDistanceManagerAccessor", "debug.accessor.ChunkMapAccessor", "debug.TicketMixin", "debug.TicketOwnerMixin", "debug.LevelMixin"}, config = "debugChunkLoading")
public class DebugChunkLoading {
	private static final Map<ResourceKey<Level>, LongSet> trackingChunk = new HashMap<>();
	public static BlockPos lastBlockPos = BlockPos.ZERO;

	public DebugChunkLoading() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(EventPriority.HIGHEST, this::onChunkLoad);
		gameBus.addListener(EventPriority.HIGHEST, this::onChunkUnload);
		gameBus.addListener(this::ticketChange);
	}

	public static void addTrackingChunk(ResourceKey<Level> dimension, ChunkPos chunkPos) {
		trackingChunk.computeIfAbsent(dimension, (key) -> new LongOpenHashSet()).add(chunkPos.toLong());
	}

	public static boolean isTracking(ResourceKey<Level> dimension, long packedPos) {
		// We track everything if it is empty
		return trackingChunk.isEmpty() || trackingChunk.getOrDefault(dimension, LongSet.of()).contains(packedPos);
	}

	public static List<ChunkPos> getTrackingChunks(ResourceKey<Level> dimension) {
		return trackingChunk.getOrDefault(dimension, LongSet.of()).longStream().mapToObj(ChunkPos::new).toList();
	}

	public static void clearTrackingChunks(){
		trackingChunk.clear();
	}

	private void ticketChange(ChunkTicketLevelUpdatedEvent event) {
		if (event.getLevel().isClientSide()) {
			return;
		}
		if (!isTracking(event.getLevel().dimension(), event.getChunkPos())) {
			return;
		}
		AllTheLeaks.LOGGER.info("Updating ticket level of (ChunkHolder@{}) chunk: {} from {} to {} at {}", Integer.toHexString(event.getChunkHolder().hashCode()), new ChunkPos(event.getChunkPos()), event.getOldTicketLevel(), event.getNewTicketLevel(), event.getLevel().dimension().location());
	}

	private void onChunkUnload(ChunkEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
			return;
		}
		if (!isTracking(((Level) event.getLevel()).dimension(), event.getChunk().getPos().toLong())) {
			return;
		}
		AllTheLeaks.LOGGER.info("Unloading chunk: {} at {}", event.getChunk().getPos(), ((Level) event.getLevel()).dimension().location());
	}

	private void onChunkLoad(ChunkEvent.Load event) {
		if (event.getLevel().isClientSide()) {
			return;
		}
		if (!isTracking(((Level) event.getLevel()).dimension(), event.getChunk().getPos().toLong())) {
			return;
		}
		AllTheLeaks.LOGGER.info("Loading chunk: {} at {}", event.getChunk().getPos(), ((Level) event.getLevel()).dimension().location());
	}
}
