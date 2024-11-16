package dev.uncandango.alltheleaks.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record SectionAndDimensionKey(int x, int z, ResourceKey<Level> dimension) {
}
