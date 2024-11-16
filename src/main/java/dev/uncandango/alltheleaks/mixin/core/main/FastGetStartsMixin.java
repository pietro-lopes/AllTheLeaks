package dev.uncandango.alltheleaks.mixin.core.main;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.uncandango.alltheleaks.config.ATLProperties;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.ChunkAccessWrapper;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.*;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.StructureAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Objects;

@Mixin(StructureManager.class)
public abstract class FastGetStartsMixin {
	@WrapOperation(
		method = "getStructureAt(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/Structure;)Lnet/minecraft/world/level/levelgen/structure/StructureStart;",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/StructureManager;startsForStructure(Lnet/minecraft/core/SectionPos;Lnet/minecraft/world/level/levelgen/structure/Structure;)Ljava/util/List;")
	)
	private List<StructureStart> atl$startsForStructure(StructureManager instance, SectionPos sectionPos, Structure structure, Operation<List<StructureStart>> original) {
		if (sectionPos.x() == 0 && sectionPos.z() == 0) {
			return original.call(instance, sectionPos, structure);
		} else {
			ImmutableList.Builder<StructureStart> builder = ImmutableList.builder();
			Objects.requireNonNull(builder);
			for (long i : atl$cachedChunks(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES, level.dimensionType().effectsLocation().toString()).getReferencesForStructure(structure)) {
				SectionPos sectionpos = SectionPos.of(new ChunkPos(i), level.getMinSection());
				ChunkAccess ca = atl$cachedChunks(sectionpos.x(), sectionpos.z(), ChunkStatus.STRUCTURE_STARTS, level.dimensionType().effectsLocation().toString());
				StructureStart structurestart = this.getStartForStructure(sectionpos, structure, ca);
				if (structurestart != null && structurestart.isValid()) {
					builder.add(structurestart);
				}
			}
			return builder.build();
		}
	}
	private ChunkAccess atl$cachedChunks(int x, int z, ChunkStatus s, String d) {
		long pos = ChunkPos.asLong(x, z);
		for (int i = 0; i < cacheSize; i++) {
			if (cache[i] == null) break;
			if (cache[i].pos == pos) {
				if (Objects.equals(cache[i].dim, d)) {
					return cache[i].chunk;
				}
				break;
			}
		}
		if (lastIndex >= cacheSize) {
			lastIndex = 0;
		}
		ChunkAccessWrapper cr = new ChunkAccessWrapper(pos, level.getChunk(x, z, s), level.dimensionType().effectsLocation().toString());
		cache[lastIndex] = cr;
		lastIndex++;
		return cr.chunk;
	}


	@Shadow
	abstract StructureStart getStartForStructure(SectionPos sectionPos, Structure structure, StructureAccess structureAccess);
	@Shadow
	LevelAccessor level;
	private static final int cacheSize = ATLProperties.get().ChunkCacheSize; //number of chunks to keep in the cache
	private static final int buffer = 5; //most of the time this space will be filled with null objects, but there was a crash (once) so thusly it came into existence. i dont like it
	private static final ChunkAccessWrapper[] cache = new ChunkAccessWrapper[cacheSize + buffer];
	private static int lastIndex = 0;
}