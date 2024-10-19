package dev.uncandango.alltheleaks.mods;

import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.uncandango.alltheleaks.mixin.core.accessor.BaseScreenAccessor;
import net.minecraft.client.gui.screens.Screen;

public interface FtbLibrary {
    static void setPrevScreen(Screen screen){
        if (GuiHelper.BLANK_GUI instanceof BaseScreenAccessor accessor){
            accessor.atl$setprevScreen(screen);
        }
    }
}
