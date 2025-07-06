package dev.uncandango.alltheleaks.fix.common.mods.modernfix;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "modernfix", versionRange = "[5.24.0+mc1.21.1,5.24.1+mc1.21.1]", mixins = "main.IngredientValueDeduplicatorMixin", extraModDep = "owo", extraModDepVersions = "*")
public class FixPrototypeExtension {
}
