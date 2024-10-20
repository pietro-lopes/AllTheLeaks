package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.gregtechceu.gtceu.api.gui.widget.PatternPreviewWidget;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashMap;
import java.util.Map;

@Pseudo
@Mixin(value = PatternPreviewWidget.class, remap = false)
public interface PatternPreviewWidgetAccessor {
    @Mutable
    @Accessor("LEVEL")
    static void atl$setLevel(TrackedDummyWorld world) {
    }

    @Accessor("LEVEL")
    static TrackedDummyWorld atl$getLevel() {
        throw new AssertionError();
    }

    @Accessor("CACHE")
    static Map<MultiblockMachineDefinition, Object[]> atl$getCache() {
        return new HashMap<>();
    }
}
