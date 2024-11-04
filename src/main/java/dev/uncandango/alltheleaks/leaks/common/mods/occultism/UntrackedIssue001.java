package dev.uncandango.alltheleaks.leaks.common.mods.occultism;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "occultism", versionRange = "[1.80.7,)", mixins = "main.OccultismCapabilitiesMixin",
	description = "Adds a missing invalidateCaps at `OccultismCapabilities#onPlayerClone` on player clone")
public class UntrackedIssue001 {
}
