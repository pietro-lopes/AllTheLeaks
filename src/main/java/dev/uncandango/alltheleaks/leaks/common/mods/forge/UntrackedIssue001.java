package dev.uncandango.alltheleaks.leaks.common.mods.forge;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "forge", versionRange = "[47.2,)", mixins = "main.ServerPlayerMixin",
	description = "Make FakePlayers stop listening to advancements")
public class UntrackedIssue001 {
}
