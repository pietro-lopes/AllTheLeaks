package dev.uncandango.alltheleaks.feature.common.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "minecraft",  issueId = "Cache Chunks on Structure Manager", versionRange = "1.21.1", mixins = {"main.StructureManagerMixin"}, config = "experimentalStructureGeneration")
public class CacheChunkOnStructureManager {
}