package dev.uncandango.alltheleaks.fix.common.mods.enderio;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "enderio", versionRange = "*", mixins = {"main.CountedIngredientMixin", "main.WrappedEnchanterRecipeMixin"})
public class FixItemStackCopy {
}
