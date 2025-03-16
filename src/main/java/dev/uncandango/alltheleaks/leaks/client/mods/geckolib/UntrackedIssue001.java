package dev.uncandango.alltheleaks.leaks.client.mods.geckolib;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "geckolib", versionRange = "[4.4.8,)", mixins = {"main.GeoModelMixin","main.GeoArmorRendererMixin"},
description = "Uses WeakReferences to prevent memory leaks on `GeoModel#applyMolangQueries()` and updates entity at `GeoArmorRenderer#currentEntity`")
public class UntrackedIssue001 {
}
