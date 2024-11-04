package dev.uncandango.alltheleaks.leaks.common.mods.curios;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "curios", versionRange = "[5.9.1,)", mixins = "main.CuriosEventHandlerMixin",
	description = "Use reviveCaps instead of revive and invalidate on `CuriosEventHandler#playerClone`")
public class UntrackedIssue001 {
}
