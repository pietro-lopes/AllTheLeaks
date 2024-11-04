package dev.uncandango.alltheleaks.leaks.client.mods.cyclopscore;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "cyclopscore", versionRange = "[1.19.1,)", mixins = "main.DelegatingDynamicItemAndBlockModelMixin",
description = "Updates `DelegatingDynamicItemAndBlockModel#world` on client level update")
public class UntrackedIssue001 {
}
