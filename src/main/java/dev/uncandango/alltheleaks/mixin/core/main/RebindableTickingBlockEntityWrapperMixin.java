package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.RebindableTickingBlockEntityWrapperExtended;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.world.level.chunk.LevelChunk$RebindableTickingBlockEntityWrapper")
public class RebindableTickingBlockEntityWrapperMixin implements RebindableTickingBlockEntityWrapperExtended {

	@Shadow(aliases = "f_156443_")
	@Final
	LevelChunk this$0;

	@Override
	public LevelChunk atl$getLevelChunk() {
		return this$0;
	}
}
