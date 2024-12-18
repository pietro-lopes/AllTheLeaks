package dev.uncandango.alltheleaks.diag.common.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "minecraft", issueId = "Debug ItemStack modifications", versionRange = "1.21.1", mixins = {"main.ItemStackDebugMixin","main.IngredientDebugMixin"}, config = "debugItemStackModifications")
public class DebugItemStackModifications {
}
