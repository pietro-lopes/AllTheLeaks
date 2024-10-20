package dev.uncandango.alltheleaks.mods;

import com.simibubi.create.CreateClient;
import dev.uncandango.alltheleaks.mixin.core.accessor.OutlinerAccessor;

public interface Create {
    static void run() {
        if (CreateClient.OUTLINER instanceof OutlinerAccessor accessor) {
            accessor.atl$getOutlines().clear();
        }
    }
}
