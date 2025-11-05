package dev.uncandango.alltheleaks.mixin.core.debug;

import com.llamalad7.mixinextras.sugar.Local;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.diag.common.mods.minecraft.DebugChunkLoading;
import dev.uncandango.alltheleaks.mixin.TicketExtension;
import dev.uncandango.alltheleaks.mixin.core.debug.accessor.ChunkMapAccessor;
import dev.uncandango.alltheleaks.mixin.core.debug.accessor.ChunkMapDistanceManagerAccessor;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.Ticket;
import net.minecraft.world.level.ChunkPos;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(DistanceManager.class)
public class DistanceManagerMixin {

	@SuppressWarnings("resource")
	@Inject(method = "addTicket(JLnet/minecraft/server/level/Ticket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/Ticket;getTicketLevel()I", ordinal = 0))
	private void logIfInBetween(long chunkPos, Ticket<?> p_ticket, CallbackInfo ci, @Local(ordinal = 1) Ticket<?> updatedTicket) {
		if (this instanceof ChunkMapDistanceManagerAccessor accessor) {
			if (accessor.atl$getChunkMap() instanceof ChunkMapAccessor cmAccessor) {
				var dimension = cmAccessor.atl$getServerLevel().dimension();
				var trackingPos = DebugChunkLoading.getTrackingChunks(dimension);
				if (trackingPos.isEmpty()) {
					// Not tracking anything, so, track all
					AllTheLeaks.LOGGER.info("{} added to {}", updatedTicket, dimension.location());
				} else {
					var chunks = atl$inBetween(updatedTicket, trackingPos);
					if (chunks.isEmpty()) return;
					AllTheLeaks.LOGGER.info("{} was added to {} and may touch chunks: {}", updatedTicket, dimension.location(), StringUtils.join(chunks,","));
				}
			}
		}
	}

	@Unique
	@SuppressWarnings("ConstantValue")
	private List<ChunkPos> atl$inBetween(Ticket<?> ticket, List<ChunkPos> chunkPositions) {
		var rangeToCheck = 45 - ticket.getTicketLevel();
		var touchedChunks = new ArrayList<ChunkPos>();
		if ((Object) ticket instanceof TicketExtension<?> ticketExtension) {
			for (var chunk : chunkPositions) {
				if (ticketExtension.atl$isTouching(chunk, rangeToCheck)) {
					touchedChunks.add(chunk);
				}
			}
		}
		return touchedChunks;
	}
}
