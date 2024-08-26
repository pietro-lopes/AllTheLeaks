package dev.uncandango.alltheleaks.mixin.core.accessor;

import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = BaseScreen.class, remap = false)
public interface BaseScreenAccessor {
	@Mutable
	@Accessor("prevScreen")
	void atl$setPrevScreen(Screen screen);
}
