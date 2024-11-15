package dev.uncandango.alltheleaks.mixin.core.main;

import com.google.common.collect.ImmutableList;
import dev.uncandango.alltheleaks.config.ATLProperties;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.*;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.StructureAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.List;
import java.util.Objects;

@Mixin(StructureManager.class)
public abstract class FastGetChunkMixin {
	/** @noinspection UnresolvedMixinReference*/
	@Inject(
		method = "startsForStructure(Lnet/minecraft/core/SectionPos;Lnet/minecraft/world/level/levelgen/structure/Structure;)Ljava/util/List;",
		at = @At(value = "HEAD"),
		cancellable = true
	)
	private void ATL_FastGetChunk(SectionPos sectionPos, Structure structure, CallbackInfoReturnable<List<StructureStart>> ci) { //should probably be refactored to fast get structure start or something like that
		if (sectionPos.x() == 0 && sectionPos.z() == 0) { //for some reason getting the chunk at 0, 0 returns null for me, so cancel and let the normal generator handle this once in a world problem
			return;
		}
		ImmutableList.Builder<StructureStart> builder = ImmutableList.builder();
		Objects.requireNonNull(builder);
		for (long i : ATL_cachedChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES).getReferencesForStructure(structure)) {
			SectionPos sectionpos = SectionPos.of(new ChunkPos(i), level.getMinSection());
			ChunkAccess ca = ATL_cachedChunk(sectionpos.x(), sectionpos.z(), ChunkStatus.STRUCTURE_STARTS);
			StructureStart structurestart = this.getStartForStructure(sectionpos, structure, ca);
			if (structurestart != null && structurestart.isValid()) {
				builder.add(structurestart);
			}
		}
		ci.setReturnValue(builder.build());
	}
	@NotNull
	private ChunkAccess ATL_cachedChunk(int x, int z, ChunkStatus s) {
		long pos = ChunkPos.asLong(x, z);
		for (int i = 0; i < cacheSize; i++) {
			if (posArr[i] == pos) {
				return cache[i];
			}
		}
		if (lastIndex >= cacheSize) {
			lastIndex = 0;
		}
		ChunkAccess cr = level.getChunk(x, z, s);
		posArr[lastIndex] = pos;
		cache[lastIndex] = cr;
		lastIndex++;
		return cr;
	}
	@Shadow
	abstract StructureStart getStartForStructure(SectionPos sectionPos, Structure structure, StructureAccess structureAccess);
	@Shadow
	LevelAccessor level;
	private static final int cacheSize = ATLProperties.get().ChunkCacheSize; //number of chunks to keep in the cache
	private static final int buffer = 3; //most of the time this space will be filled with null objects, but there was a crash so thusly it came into existence. i dont like it
	private static final long[] posArr = new long[cacheSize + buffer];
	private static final ChunkAccess[] cache = new ChunkAccess[cacheSize + buffer];
	private static int lastIndex = 0;
}