package dev.uncandango.alltheleaks.mods;

import dev.uncandango.alltheleaks.mixin.core.accessor.AreaRenderManagerAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.ArmorMainScreenAccessor;
import me.desht.pneumaticcraft.client.gui.pneumatic_armor.ArmorMainScreen;
import me.desht.pneumaticcraft.client.render.area.AreaRenderManager;

public interface Pneumaticcraft {
    static void run() {
        if (ArmorMainScreen.getInstance() instanceof ArmorMainScreenAccessor accessor) {
            accessor.getUpgradeOptions().clear();
        }
        AreaRenderManagerAccessor.class.cast(AreaRenderManager.INSTANCE).atl$setLevel(null);
    }
}
