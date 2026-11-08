package dev.uncandango.alltheleaks.mixin;

import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

public interface RebindableTickingBlockEntityWrapperExtended {
	LevelChunk atl$getLevelChunk();
	TickingBlockEntity atl$getTicker();
}
