package dev.uncandango.alltheleaks.mods;

import com.minecolonies.core.client.render.worldevent.WorldEventContext;

public interface Minecolonies {
    static void run() {
        WorldEventContext.INSTANCE.clientPlayer = null;
        WorldEventContext.INSTANCE.clientLevel = null;
        WorldEventContext.INSTANCE.stageEvent = null;
        WorldEventContext.INSTANCE.bufferSource = null;
        WorldEventContext.INSTANCE.nearestColony = null;
        WorldEventContext.INSTANCE.mainHandItem = null;
        WorldEventContext.INSTANCE.poseStack = null;
        WorldEventContext.INSTANCE.partialTicks = 0;
    }
}
