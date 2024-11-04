package dev.uncandango.alltheleaks.leaks.common.mods.forbidden_arcanus;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "forbidden_arcanus", versionRange = "[1.20.1-2.2.0-beta1,)", mixins = "main.PlayerEventsMixin",
	description = "Adds an invalidateCaps that was missing on `PlayerEvents#onPlayerClone`")
public class UntrackedIssue001 {
}
