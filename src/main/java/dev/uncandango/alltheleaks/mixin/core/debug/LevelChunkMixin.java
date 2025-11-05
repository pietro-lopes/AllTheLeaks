package dev.uncandango.alltheleaks.mixin.core.debug;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.mixin.core.debug.accessor.LevelAccessor;
import net.minecraft.core.Registry;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin extends ChunkAccess {
	@Shadow
	@Final
	Level level;

	public LevelChunkMixin(ChunkPos chunkPos, UpgradeData upgradeData, LevelHeightAccessor levelHeightAccessor, Registry<Biome> biomeRegistry, long inhabitedTime, @Nullable LevelChunkSection[] sections, @Nullable BlendingData blendingData) {
		super(chunkPos, upgradeData, levelHeightAccessor, biomeRegistry, inhabitedTime, sections, blendingData);
	}

	@Inject(method = "registerAllBlockEntitiesAfterLevelLoad", at =@At("TAIL"))
	private void reportFreshEntitiesCount(CallbackInfo ci){
		if (this.level instanceof LevelAccessor sl) {
			AllTheLeaks.LOGGER.info("Fresh BlockEntities for {} is {}", level.dimension().location(), sl.atl$getFreshBlockEntities().size());
		}
	}
}
