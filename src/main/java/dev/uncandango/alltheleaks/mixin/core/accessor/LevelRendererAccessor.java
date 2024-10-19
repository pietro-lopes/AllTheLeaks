package dev.uncandango.alltheleaks.mixin.core.accessor;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.concurrent.BlockingQueue;

@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor {
    @Accessor("recentlyCompiledChunks")
    BlockingQueue<ChunkRenderDispatcher.RenderChunk> atl$getRecentlyCompiledChunks();
}
