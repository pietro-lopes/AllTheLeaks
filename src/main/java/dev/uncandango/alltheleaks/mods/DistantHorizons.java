package dev.uncandango.alltheleaks.mods;

import com.seibel.distanthorizons.core.render.glObject.GLProxy;
import dev.uncandango.alltheleaks.AllTheLeaks;
import loaderCommon.forge.com.seibel.distanthorizons.common.wrappers.chunk.ChunkWrapper;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;

public interface DistantHorizons {
    static void run() {
        try {
            // these tasks always need to be called, regardless of whether the renderer is enabled or not to prevent memory leaks
            LogicalSidedProvider.WORKQUEUE.get(LogicalSide.CLIENT).execute(() -> GLProxy.getInstance().runRenderThreadTasks());
            ChunkWrapper.syncedUpdateClientLightStatus();
        } catch (Exception e) {
            AllTheLeaks.LOGGER.error("Unexpected issue running render thread tasks.", e);
        }
    }
}
