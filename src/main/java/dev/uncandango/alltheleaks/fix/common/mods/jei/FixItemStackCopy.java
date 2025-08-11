package dev.uncandango.alltheleaks.fix.common.mods.jei;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "jei", versionRange = "*", mixins = {"main.SmithingCategoryExtensionMixin", "main.NormalizedTypedItemStackMixin", "main.IngredientSetMixin"})
public class FixItemStackCopy {
}
