package dev.uncandango.alltheleaks.leaks.common.mods.ars_nouveau;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "ars_nouveau", versionRange = "[4.0.0,4.12.4]", mixins = "main.CasterTomeRegistryMixin",
description = "Rework lambda to not leak level on `CasterTomeRegistry#reloadTomeData`")
public class UntrackedIssue002 {
}
