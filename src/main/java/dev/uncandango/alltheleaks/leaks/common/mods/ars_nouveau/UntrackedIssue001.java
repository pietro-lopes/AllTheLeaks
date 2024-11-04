package dev.uncandango.alltheleaks.leaks.common.mods.ars_nouveau;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "ars_nouveau", versionRange = "[4.0.0,4.12.4]", mixins = "main.EventHandlerMixin",
description = "Uses reviveCaps instead of revive on `CapabilityRegistry.EventHandler#playerClone()`")
public class UntrackedIssue001 {
}
