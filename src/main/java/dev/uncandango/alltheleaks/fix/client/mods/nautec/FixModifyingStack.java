package dev.uncandango.alltheleaks.fix.client.mods.nautec;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "nautec", mixins = "main.RecipeUtilsMixin", versionRange = "[0.2.9]", extraModDep = {"jei"}, extraModDepVersions = {"*"})
public class FixModifyingStack {
}
