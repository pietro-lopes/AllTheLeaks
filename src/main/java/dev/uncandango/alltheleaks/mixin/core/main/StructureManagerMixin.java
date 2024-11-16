package dev.uncandango.alltheleaks.mixin.core.main;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.uncandango.alltheleaks.mixin.SectionAndDimensionKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StructureManager.class)
public abstract class StructureManagerMixin {

	@Unique
	private static final Cache<SectionAndDimensionKey, ChunkAccess> atl$cache = CacheBuilder.newBuilder().weakValues().build();
	@Shadow
	@Final
	private LevelAccessor level;

//	@WrapOperation(
//		method = {"startsForStructure(Lnet/minecraft/core/SectionPos;Lnet/minecraft/world/level/levelgen/structure/Structure;)Ljava/util/List;", "fillStartsForStructure"},
//		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;getChunk(IILnet/minecraft/world/level/chunk/status/ChunkStatus;)Lnet/minecraft/world/level/chunk/ChunkAccess;")
//	)
//	private ChunkAccess atl$getCachedChunk(LevelAccessor instance, int x, int z, ChunkStatus chunkStatus, Operation<ChunkAccess> original) {
//		ChunkAccess chunk;
//		ChunkAccess chunkOriginal;
//		SectionKey sectionKey = null;
//		if (this.level instanceof WorldGenLevel genLevel) {
//			sectionKey = new SectionKey(x, z, genLevel.getLevel().dimension());
//		}
//		chunk = atl$cache.getIfPresent(sectionKey);
//		var timer = Stopwatch.createStarted();
//		chunkOriginal = original.call(instance, x, z, chunkStatus);
//		if (chunk != null & timer.elapsed().toMillis() > 1000) {
//			AllTheLeaks.LOGGER.warn("Chunk took over 1000 ms to get and was already cached! {}", sectionKey);
//		}
//		if (chunk == null) {
//			atl$cache.put(sectionKey, chunkOriginal);
//			chunk = chunkOriginal;
//		}
//		if (chunk != chunkOriginal) {
//			AllTheLeaks.LOGGER.warn("Chunk is not equal! {}", sectionKey);
//		}
//		return chunkOriginal;
//	}

	@WrapOperation(
		method = {"startsForStructure(Lnet/minecraft/core/SectionPos;Lnet/minecraft/world/level/levelgen/structure/Structure;)Ljava/util/List;", "fillStartsForStructure"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;getChunk(IILnet/minecraft/world/level/chunk/status/ChunkStatus;)Lnet/minecraft/world/level/chunk/ChunkAccess;")
	)
	private ChunkAccess atl$getCachedChunk(LevelAccessor instance, int x, int z, ChunkStatus chunkStatus, Operation<ChunkAccess> original) {
		ChunkAccess chunk = null;
		SectionAndDimensionKey sectionKey = null;
		if (this.level instanceof WorldGenLevel genLevel) {
			sectionKey = new SectionAndDimensionKey(x, z, genLevel.getLevel().dimension());
			chunk = atl$cache.getIfPresent(sectionKey);
		}
		if (chunk == null) {
			chunk = original.call(instance, x, z, chunkStatus);
			if (sectionKey != null) {
				atl$cache.put(sectionKey, chunk);
			}
		}
		return chunk;
	}
}