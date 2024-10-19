package dev.uncandango.alltheleaks.mixin.core.accessor;

import me.lucko.spark.common.monitor.ping.PingStatistics;
import me.lucko.spark.common.monitor.ping.PlayerPingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = PingStatistics.class, remap = false)
public interface PingStatisticsAccessor {
    @Accessor("provider")
    PlayerPingProvider atl$getProvider();
}
