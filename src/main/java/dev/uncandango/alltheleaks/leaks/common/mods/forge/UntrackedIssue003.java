package dev.uncandango.alltheleaks.leaks.common.mods.forge;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "forge", versionRange = "*", description = "Not a leak exactly but heavy allocation (by Arug)", mixins = {"main.EntityMixin2","main.RandomizableContainerBlockEntityMixin"})
public class UntrackedIssue003 {
}
