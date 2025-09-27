package dev.uncandango.alltheleaks.feature.common.mods.minecraft;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.RebindableTickingBlockEntityWrapperExtended;
import dev.uncandango.alltheleaks.mixin.core.accessor.LevelAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.LevelChunkAccessor;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraftforge.server.ServerLifecycleHooks;

@Issue(modId = "minecraft", versionRange = "1.20.1", mixins = {"accessor.LevelAccessor", "main.RebindableTickingBlockEntityWrapperMixin","accessor.LevelChunkAccessor"})
public class ClearLeakedLevelChunks {
	public static void execute(){
		final var server = ServerLifecycleHooks.getCurrentServer();
		if (server == null) {
			AllTheLeaks.LOGGER.warn("Server not found while trying to clear leaked chunks");
			return;
		}
		server.executeBlocking(() -> {
			for (var level : server.getAllLevels()) {
				if (level instanceof LevelAccessor levelAccessor) {
					levelAccessor.atl$getBlockEntityTickers().removeIf(ClearLeakedLevelChunks::isNullTicker);
				}
			}
		});
	}

	static boolean isNullTicker(TickingBlockEntity tickingBlockEntity) {
		if (tickingBlockEntity == LevelChunkAccessor.atl$getNullTicker()) {
			if (tickingBlockEntity instanceof RebindableTickingBlockEntityWrapperExtended tickingAccessor) {
				var levelChunk = tickingAccessor.atl$getLevelChunk();
				if (levelChunk instanceof LevelChunkAccessor chunkAccessor) {
					if (!chunkAccessor.atl$isInLevel()) {
						AllTheLeaks.LOGGER.debug("Cleared leaked chunk at {} from dimension {}", levelChunk.getPos(), levelChunk.getLevel().dimension().location());
						return true;
					}
				}
			}
		}
		return false;
	}

}
