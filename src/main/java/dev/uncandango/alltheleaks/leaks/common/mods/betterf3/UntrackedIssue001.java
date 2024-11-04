package dev.uncandango.alltheleaks.leaks.common.mods.betterf3;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "betterf3", versionRange = "[7.0.2,)", mixins = "main.LocationModuleMixin",
description = "Reworked `LocationModule#update()` to prevent leak of current chunk")
public class UntrackedIssue001 {
}
