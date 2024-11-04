package dev.uncandango.alltheleaks.leaks.common.mods.aether;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "aether", versionRange = "[1.20.1-1.4.2,)", mixins = "main.DroppedItemCapabilityMixin",
description = "Reworked `DroppedItemCapability` to not leak entities")
public class UntrackedIssue001 {

}
