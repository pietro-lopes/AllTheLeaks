package dev.uncandango.alltheleaks.fix.client.mods.apotheosis;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "apotheosis", mixins = "main.PotionCharmExtensionMixin", versionRange = "(,8.4.0]", extraModDep = {"jei"}, extraModDepVersions = {"*"})
public class FixModifyingStack {
}
