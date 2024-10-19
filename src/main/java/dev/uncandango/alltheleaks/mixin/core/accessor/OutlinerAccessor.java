package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.simibubi.create.foundation.outliner.Outliner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Pseudo
@Mixin(value = Outliner.class, remap = false)
public interface OutlinerAccessor {
    @Accessor("outlines")
    Map<Object, Outliner.OutlineEntry> atl$getOutlines();
}
