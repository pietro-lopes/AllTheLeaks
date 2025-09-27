package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.github.alexthe666.citadel.repack.jcodec.codecs.mjpeg.tools.AssertionException;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelChunk.class)
public interface LevelChunkAccessor {
	@Accessor("NULL_TICKER")
	static TickingBlockEntity atl$getNullTicker(){
		throw new AssertionException("Accessor not applied");
	}

	@Invoker("isInLevel")
	boolean atl$isInLevel();
}
