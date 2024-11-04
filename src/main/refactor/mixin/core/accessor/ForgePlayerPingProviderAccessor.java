package dev.uncandango.alltheleaks.mixin.core.accessor;

import me.lucko.spark.forge.ForgePlayerPingProvider;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = ForgePlayerPingProvider.class, remap = false)
public interface ForgePlayerPingProviderAccessor {
    @Mutable
    @Accessor("server")
    void atl$setServer(MinecraftServer server);
}
