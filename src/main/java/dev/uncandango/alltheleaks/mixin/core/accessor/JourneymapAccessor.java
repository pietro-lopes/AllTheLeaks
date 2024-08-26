package dev.uncandango.alltheleaks.mixin.accessor;

import journeymap.common.Journeymap;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Journeymap.class, remap = false)
public interface JourneymapAccessor {
	@Mutable
	@Accessor("server")
	void atl$setServer(MinecraftServer server);
}
