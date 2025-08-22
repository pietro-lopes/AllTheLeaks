package dev.uncandango.alltheleaks.mixin.core.accessor;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(PatchedDataComponentMap.class)
public interface PatchedDataComponentMapAccessor {
	@Accessor("prototype")
	DataComponentMap atl$getPrototype();

	@Accessor("patch")
	Reference2ObjectMap<DataComponentType<?>, Optional<?>> atl$getPatch();
}
