package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.Trackable;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkAccess.class)
public class ChunkAccessMixin implements Trackable {
	@Override
	public Class<?> atl$getBaseClass() {
		return ChunkAccess.class;
	}
}
