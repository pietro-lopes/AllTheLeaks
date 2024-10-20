package dev.uncandango.alltheleaks.mods;

import dev.uncandango.alltheleaks.mixin.core.accessor.EntityComparatorAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.JourneymapAccessor;
import journeymap.client.model.EntityHelper;
import journeymap.client.task.multi.RenderSpec;

public interface Journeymap {
    static void run() {
        if (journeymap.common.Journeymap.getInstance() instanceof JourneymapAccessor accessor) {
            accessor.atl$setServer(null);
        }
        if (EntityHelper.entityDistanceComparator instanceof EntityComparatorAccessor accessor) {
            accessor.setPlayer(null);
        }
        if (EntityHelper.entityDTODistanceComparator instanceof EntityComparatorAccessor accessor) {
            accessor.setPlayer(null);
        }
        RenderSpec.resetRenderSpecs();
    }
}
