package dev.uncandango.alltheleaks.leaks.common.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "minecraft", versionRange = "1.20.1", mixins = "main.EntityTickListMixin",
	description = "Clears temporary entity tick list after done looping")
public class UntrackedIssue002 {
}
