package dev.uncandango.alltheleaks.leaks.client.mods.flywheel;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "flywheel", versionRange = "[0.6.9-4,)", mixins = "main.WorldAttachedMixin",
	description = "Prevents old world to be added to the world list via `WorldAttached#put()`")
public class UntrackedIssue001 {
}
