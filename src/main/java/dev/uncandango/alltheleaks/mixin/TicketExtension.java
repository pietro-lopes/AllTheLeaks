package dev.uncandango.alltheleaks.mixin;

import net.minecraft.world.level.ChunkPos;

public interface TicketExtension<T> {
	T atl$getKey();

	default boolean atl$isTouching(ChunkPos chunkpos, int range) {
		if (atl$getKey() instanceof ChunkPos key) {
			// key: 38, -90
			// range 12
			// chunkpos: 46, -80
			return (chunkpos.x > key.x - range && // -80 < -78 -> Pass
				chunkpos.x < key.x + range) && // 46 > 26 -> Pass
				(chunkpos.z > key.z - range && // 46 < 50 -> Pass
					chunkpos.z < key.z + range); // -80 > -102 -> Pass
		}
		return false;
	}
}
