package dev.uncandango.alltheleaks.mixin.core.main;

import com.google.common.collect.ImmutableList;
import dev.uncandango.alltheleaks.AllTheLeaks;
import it.unimi.dsi.fastutil.longs.LongSet;
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
public abstract class fastGetChunk {
	@Inject(
		method = "Lnet/minecraft/world/level/StructureManager;startsForStructure(Lnet/minecraft/core/SectionPos;Lnet/minecraft/world/level/levelgen/structure/Structure;)Ljava/util/List;",
		at = @At(value = "HEAD"),
		cancellable = true
	)
	private void ATL_fastGetChunk(SectionPos sectionPos, Structure structure, CallbackInfoReturnable<List<StructureStart>> ci) {
		if (sectionPos.x() == 0 && sectionPos.z() == 0) {
			ci.cancel();
		}
		lr = ATL_cachedReferenceChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES).getReferencesForStructure(structure);
		ImmutableList.Builder<StructureStart> builder = ImmutableList.builder();
		Objects.requireNonNull(builder);
		for (long i : lr) {
			SectionPos sectionpos = SectionPos.of(new ChunkPos(i), this.level.getMinSection());
			StructureStart structurestart = this.getStartForStructure(sectionpos, structure, ATL_cachedStartChunk(sectionpos.x(), sectionpos.z(), ChunkStatus.STRUCTURE_STARTS));
			if (structurestart != null && structurestart.isValid()) {
				builder.add(structurestart);
			}
		}
		ci.setReturnValue(builder.build());
	}
	@NotNull
	private ChunkAccess ATL_cachedReferenceChunk(int x, int z, ChunkStatus s) {
		pos = ChunkPos.asLong(x, z);
		for (int i = 0; i < cacheSize; i++) {
			if (posArr[i] == pos) {
				return cache[i];
			}
		}
		if (lastIndex >= cacheSize) {
			lastIndex = 0;
		}
		//AllTheLeaks.LOGGER.info("ref chunk " + x + ", " + z);
		cr = level.getChunk(x, z, s);
		posArr[lastIndex] = pos;
		cache[lastIndex] = cr;
		lastIndex++;
		return cr;
	}
	@NotNull
	private ChunkAccess ATL_cachedStartChunk(int x, int z, ChunkStatus s) {
		pos = ChunkPos.asLong(x, z);
		for (int i = 0; i < cacheSize; i++) {
			if (posArr[i] == pos) {
				return cache[i];
			}
		}
		if (lastIndex >= cacheSize) {
			lastIndex = 0;
		}
		//AllTheLeaks.LOGGER.info("start chunk " + x + ", " + z);
		cr = level.getChunk(x, z, s);
		posArr[lastIndex] = pos;
		cache[lastIndex] = cr;
		lastIndex++;
		return cr;
	}
	@Shadow
	abstract StructureStart getStartForStructure(SectionPos sectionPos, Structure structure, StructureAccess structureAccess);
	@Shadow
	LevelAccessor level;
	private static long pos = 0;
	LongSet lr;
	private static final int size = 16;
	private static final int buffer = 4;
	private static final int cacheSize = size - buffer;
	private static final long[] posArr = new long[size];
	private static final ChunkAccess[] cache = new ChunkAccess[size];
	private static int lastIndex = 0;
	private static ChunkAccess cr;
}