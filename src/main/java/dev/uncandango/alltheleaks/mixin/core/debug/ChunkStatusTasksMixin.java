package dev.uncandango.alltheleaks.mixin.core.debug;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.uncandango.alltheleaks.AllTheLeaks;
import net.minecraft.server.level.ChunkTaskPriorityQueueSorter;
import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.chunk.status.ChunkStatusTasks;
import net.minecraft.world.level.chunk.status.WorldGenContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.IntSupplier;

import static dev.uncandango.alltheleaks.diag.common.mods.minecraft.DebugChunkLoading.isTracking;

@Mixin(ChunkStatusTasks.class)
public class ChunkStatusTasksMixin {
	@WrapOperation(method = "lambda$full$3", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkTaskPriorityQueueSorter;message(Ljava/lang/Runnable;JLjava/util/function/IntSupplier;)Lnet/minecraft/server/level/ChunkTaskPriorityQueueSorter$Message;"))
	private static ChunkTaskPriorityQueueSorter.Message<?> logPromotingToFull(Runnable task, long pos, IntSupplier level, Operation<ChunkTaskPriorityQueueSorter.Message<Runnable>> original, @Local(argsOnly = true) ChunkPos chunkpos, @Local(argsOnly = true) GenerationChunkHolder holder, @Local(argsOnly = true) WorldGenContext worldGenContext){
		if (isTracking(worldGenContext.level().dimension(), chunkpos.toLong())) {
			AllTheLeaks.LOGGER.info("Promoting ({}) chunk {} to full", holder.getClass().getSimpleName() + "@" + Integer.toHexString(holder.hashCode()), chunkpos);
		}
		return original.call(task, pos, level);
	}
}
