package dev.uncandango.alltheleaks.mixin.core.debug.accessor;

import net.minecraft.server.level.ChunkMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.server.level.ChunkMap$DistanceManager")
public interface ChunkMapDistanceManagerAccessor {
	@Accessor("this$0")
	ChunkMap atl$getChunkMap();
}
