package dev.uncandango.alltheleaks.leaks.common.mods.toolbelt;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "toolbelt", versionRange = "[1.20.0,)", mixins = "main.EventHandlersMixin",
	description = "Use reviveCaps instead of revive and adds invalidateCaps at `BeltExtensionSlot.EventHandlers#playerClone`")
public class UntrackedIssue001 {
}
