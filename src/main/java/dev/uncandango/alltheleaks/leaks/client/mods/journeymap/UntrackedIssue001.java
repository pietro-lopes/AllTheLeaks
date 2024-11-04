package dev.uncandango.alltheleaks.leaks.client.mods.journeymap;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "journeymap", versionRange = "[5.9.21,)", mixins = {"accessor.EntityComparatorAccessor", "main.EntityHelperMixin"},
description = "Clears `EntityDistanceComparator#player` and `EntityDTODistanceComparator#player` after usage")
public class UntrackedIssue001 {
}
