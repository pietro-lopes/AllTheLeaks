package dev.uncandango.alltheleaks.mixin.core.accessor;

import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = ClientMagicData.class, remap = false)
public interface ClientMagicDataAccessor {
    @Mutable
    @Accessor("spellSelectionManager")
    static void atl$setSpellSelectionManager(SpellSelectionManager manager) {
    }

}
