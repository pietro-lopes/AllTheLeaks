package dev.uncandango.alltheleaks.leaks.client.mods.ldlib;

import dev.uncandango.alltheleaks.annotation.Issue;

// 1.0.25.p
// 1.0.26.b
@Issue(modId = "ldlib", versionRange = "[1.0.25.p,)", mixins = {"main.DummyWorldMixin", "main.ModularUIMixin"},
description = "Refactor a lambda that leaks level and updates `ModularUI#entityPlayer` on player update")
public class UntrackedIssue001 {
}
