package dev.uncandango.alltheleaks.leaks.common.mods.forge;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "forge", issueId = "#10745", versionRange = "(,47.4.15)", description = "Not a leak exactly but heavy allocation (by Arug)", mixins = {"main.EntityMixin"})
public class Issue10745 {
}
