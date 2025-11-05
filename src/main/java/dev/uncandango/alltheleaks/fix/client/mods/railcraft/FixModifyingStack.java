package dev.uncandango.alltheleaks.fix.client.mods.railcraft;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "railcraft", mixins = "main.DefaultRecipeWrapperMixin", versionRange = "1.2.2", extraModDep = {"jei"}, extraModDepVersions = {"*"})
public class FixModifyingStack {
}
