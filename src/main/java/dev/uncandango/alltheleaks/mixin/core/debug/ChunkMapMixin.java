package dev.uncandango.alltheleaks.mixin.core.debug;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.DataFixer;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.diag.common.mods.minecraft.DebugChunkLoading;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.GeneratingChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.storage.ChunkStorage;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;
import java.util.Queue;
import java.util.function.BooleanSupplier;

import static dev.uncandango.alltheleaks.diag.common.mods.minecraft.DebugChunkLoading.isTracking;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin extends ChunkStorage implements ChunkHolder.PlayerProvider, GeneratingChunkMap {
	@Shadow
	@Final
	ServerLevel level;
	@Shadow
	@Final
	LongSet toDrop;
	@Shadow
	@Final
	private Queue<Runnable> unloadQueue;
	@Shadow
	@Final
	private Long2ObjectLinkedOpenHashMap<ChunkHolder> pendingUnloads;

	public ChunkMapMixin(RegionStorageInfo info, Path folder, DataFixer fixerUpper, boolean sync) {
		super(info, folder, fixerUpper, sync);
	}

	@Shadow
	protected abstract void scheduleUnload(long chunkPos, ChunkHolder chunkHolder);

	@Inject(method = "processUnloads", at = @At(value = "INVOKE", target = "Ljava/util/Queue;size()I"))
	private void logTimeAndQueueSize(BooleanSupplier hasMoreTime, CallbackInfo ci) {
		var queueSize = this.unloadQueue.size();
		var dropSize = this.toDrop.size();
		if (queueSize > 0 || dropSize > 0) {
			AllTheLeaks.LOGGER.info("Processing Unloads... HasMoreTime: {}, Queue size: {}, Drop size: {}", hasMoreTime.getAsBoolean(), queueSize, dropSize);
		}
	}

	@Inject(method = "lambda$scheduleUnload$12", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/longs/Long2ObjectLinkedOpenHashMap;remove(JLjava/lang/Object;)Z"))
	private void logAboutToBeUnloaded(ChunkHolder chunkHolder, long chunkPos, CallbackInfo ci, @Local ChunkAccess chunkaccess) {
		if (DebugChunkLoading.isTracking(this.level.dimension(), chunkPos)) {
			var pos = new ChunkPos(chunkPos);
			AllTheLeaks.LOGGER.info("Preparing to unload {}:", pos);
			if (chunkaccess == null) {
				AllTheLeaks.LOGGER.info(" -- ChunkAccess from ChunkHolder@{} was null", Integer.toHexString(chunkHolder.hashCode()));
			} else {
				var currentHolder = this.pendingUnloads.get(chunkPos);
				if (currentHolder == chunkHolder) {
					if (chunkaccess instanceof LevelChunk) {
						AllTheLeaks.LOGGER.info(" -- All good for ChunkHolder@{} to be unloaded", Integer.toHexString(chunkHolder.hashCode()));
					} else {
						AllTheLeaks.LOGGER.info(" -- {} from {} was NOT LevelChunk", chunkaccess.getClass().getSimpleName() + "@" + Integer.toHexString(chunkaccess.hashCode()), currentHolder.getClass().getSimpleName() + "@" +Integer.toHexString(currentHolder.hashCode()));
					}
				} else {
					AllTheLeaks.LOGGER.info(" -- Current ChunkHolder@{} is not the same as tried to unload ChunkHolder@{}", currentHolder == null ? "null" : Integer.toHexString(currentHolder.hashCode()) , Integer.toHexString(chunkHolder.hashCode()));
				}
			}

		}
	}

	@Inject(method = "updateChunkScheduling", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkHolder;setTicketLevel(I)V", ordinal = 1))
	private void logRecoveredChunk(long chunkPos, int newLevel, ChunkHolder holder, int oldLevel, CallbackInfoReturnable<ChunkHolder> cir) {
		if (!isTracking(this.level.dimension(), chunkPos)) {
			return;
		}
		AllTheLeaks.LOGGER.info("Recovered (ChunkHolder@{}) chunk {} at {} from queue set", Integer.toHexString(holder.hashCode()), new ChunkPos(chunkPos), this.level.dimension().location());
	}

	@ModifyExpressionValue(method = "updateChunkScheduling", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/longs/LongSet;remove(J)Z"))
	private boolean logRecoveredChunkToDrop(boolean original, @Local(argsOnly = true) long chunkPos, @Local(argsOnly = true) ChunkHolder holder) {
		if (original && isTracking(this.level.dimension(), chunkPos)) {
			AllTheLeaks.LOGGER.info("Recovered (ChunkHolder@{}) chunk {} at {} from drop set", Integer.toHexString(holder.hashCode()), new ChunkPos(chunkPos), this.level.dimension().location());
		}
		return original;
	}

	@Definition(id = "ChunkHolder", type = ChunkHolder.class)
	@Expression("new ChunkHolder(?, ?, ?,?,?,?)")
	@ModifyExpressionValue(method = "updateChunkScheduling", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
	private ChunkHolder logNewChunkHolder(ChunkHolder original, @Local(argsOnly = true) long chunkPos) {
		if (!isTracking(this.level.dimension(), chunkPos)) {
			return original;
		}
		AllTheLeaks.LOGGER.info("Created new ({}) chunk {} at {}", original.getClass().getSimpleName() + "@" + Integer.toHexString(original.hashCode()), new ChunkPos(chunkPos), this.level.dimension().location());
		return original;
	}

	@Inject(method = "scheduleUnload", at = @At(value = "HEAD"))
	private void logScheduleUnloadChunk(long chunkPos, ChunkHolder chunkHolder, CallbackInfo ci) {
		if (!isTracking(this.level.dimension(), chunkPos)) {
			return;
		}
		AllTheLeaks.LOGGER.info("Scheduled unload (ChunkHolder@{}) chunk {} at {}", Integer.toHexString(chunkHolder.hashCode()), new ChunkPos(chunkPos), this.level.dimension().location());
	}

}
