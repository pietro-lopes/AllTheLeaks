package dev.uncandango.alltheleaks.fix.client.mods.enderio;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "enderio", mixins = "main.WrappedEnchanterRecipeMixin", versionRange = "[7.1.8-alpha]", extraModDep = {"jei"}, extraModDepVersions = {"*"})
public class FixModifyingStack {
}

