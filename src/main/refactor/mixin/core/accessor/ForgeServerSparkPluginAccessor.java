package dev.uncandango.alltheleaks.mixin.core.accessor;

import me.lucko.spark.forge.plugin.ForgeServerSparkPlugin;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = ForgeServerSparkPlugin.class, remap = false)
public interface ForgeServerSparkPluginAccessor {
    @Mutable
    @Accessor("server")
    void atl$setServer(MinecraftServer server);
}
