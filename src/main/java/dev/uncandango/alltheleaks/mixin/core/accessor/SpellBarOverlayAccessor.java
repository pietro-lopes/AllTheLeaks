package dev.uncandango.alltheleaks.mixin.core.accessor;

import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.gui.overlays.SpellBarOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = SpellBarOverlay.class, remap = false)
public interface SpellBarOverlayAccessor {
    @Mutable
    @Accessor("lastSelection")
    static void atl$setLastSelection(SpellSelectionManager manager) {
    }

}
