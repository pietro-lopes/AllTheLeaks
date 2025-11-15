package dev.uncandango.alltheleaks.mixin.core.debug;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.uncandango.alltheleaks.diag.common.mods.minecraft.DebugChunkLoading;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Level.class)
public class LevelMixin {
	@WrapOperation(method = "getBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getChunk(II)Lnet/minecraft/world/level/chunk/LevelChunk;"))
	private LevelChunk grabBlockPosContext(Level instance, int chunkX, int chunkZ, Operation<LevelChunk> original, @Local(argsOnly = true) BlockPos pos){
		DebugChunkLoading.lastBlockPos = pos;
		var chunk = original.call(instance, chunkX, chunkZ);
		DebugChunkLoading.lastBlockPos = BlockPos.ZERO;
		return chunk;
	}

	@WrapOperation(method = {"getBlockEntity","blockEntityChanged","getFluidState"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getChunkAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/chunk/LevelChunk;"))
	private LevelChunk grabBlockPosContext2(Level instance, BlockPos pos, Operation<LevelChunk> original){
		DebugChunkLoading.lastBlockPos = pos;
		var chunk = original.call(instance, pos);
		DebugChunkLoading.lastBlockPos = BlockPos.ZERO;
		return chunk;
	}

//	@WrapOperation(method = "blockEntityChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getChunkAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/chunk/LevelChunk;"))
//	private LevelChunk grabBlockPosContext3(Level instance, BlockPos pos, Operation<LevelChunk> original){
//		DebugChunkLoading.lastBlockPos = pos;
//		var chunk = original.call(instance, pos);
//		DebugChunkLoading.lastBlockPos = BlockPos.ZERO;
//		return chunk;
//	}
//
//	@WrapOperation(method = "getFluidState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getChunkAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/chunk/LevelChunk;"))
//	private LevelChunk grabBlockPosContext4(Level instance, BlockPos pos, Operation<LevelChunk> original){
//		DebugChunkLoading.lastBlockPos = pos;
//		var chunk = original.call(instance, pos);
//		DebugChunkLoading.lastBlockPos = BlockPos.ZERO;
//		return chunk;
//	}
}
