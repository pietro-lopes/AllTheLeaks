package dev.uncandango.alltheleaks.leaks.common.mods.zeta;

import dev.uncandango.alltheleaks.annotation.Issue;
// https://github.com/VazkiiMods/Zeta/pull/69 (credits to Embeddedt)
@Issue(issueId = "#69", modId = "zeta", versionRange = "[1.0-19,)", mixins = "main.RecipeCrawlHandlerMixin", description = "Clears static maps `RecipeCrawlHandler#vanillaRecipeDigestion/backwardsVanillaDigestion` after usage")
public class Issue69 {
}
