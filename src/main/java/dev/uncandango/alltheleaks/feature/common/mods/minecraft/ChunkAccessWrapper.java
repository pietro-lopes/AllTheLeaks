package dev.uncandango.alltheleaks.feature.common.mods.minecraft;


import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.dimension.DimensionType;

public class ChunkAccessWrapper {
	public long pos;
	public ChunkAccess chunk;
	public String dim;
	public ChunkAccessWrapper(long pos, ChunkAccess chunk, String dim) {
		this.pos = pos;
		this.chunk = chunk;
		this.dim = dim;
	}
}
