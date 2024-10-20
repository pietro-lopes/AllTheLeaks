package dev.uncandango.alltheleaks.mixin.core.main;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.map.mods.SupportXaeroMinimap;

@Pseudo
@Mixin(value = SupportXaeroMinimap.class, remap = false)
public class SupportXaerosMinimapMixin {
    @Shadow private WaypointWorld mouseBlockWaypointWorld;

    @Shadow private WaypointWorld rightClickWaypointWorld;

    @Inject(method = "onSessionFinalized", at = @At("TAIL"))
    private void atl$clearMoreWorlds(CallbackInfo ci){
        mouseBlockWaypointWorld = null;
        rightClickWaypointWorld = null;
    }
}
