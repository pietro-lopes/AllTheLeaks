package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.core.accessor.ForgePlayerPingProviderAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.ForgeServerSparkPluginAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.PingStatisticsAccessor;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.forge.plugin.ForgeSparkPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = ForgeSparkPlugin.class, remap = false)
public abstract class ForgeSparkPluginMixin {

    @Shadow
    protected SparkPlatform platform;

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "disable", at = @At("TAIL"))
    void atl$clearServer(CallbackInfo ci) {
        if ((Object) this instanceof ForgeServerSparkPluginAccessor accessor) {
            accessor.atl$setServer(null);
        }
        var ping = this.platform.getPingStatistics();
        if ((Object) ping instanceof PingStatisticsAccessor accessor) {
            var provider = accessor.atl$getProvider();
            if ((Object) provider instanceof ForgePlayerPingProviderAccessor providerAccessor) {
                providerAccessor.atl$setServer(null);
            }
        }
    }
}
