package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.google.common.collect.Interner;
import dev.latvian.mods.kubejs.recipe.filter.RegexIDFilter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = RegexIDFilter.class, remap = false)
public interface RegexIDFilterAccessor {

    @Mutable
    @Accessor("INTERNER")
    static void atl$setInterner(Interner<RegexIDFilter> interner) {
    }

}
