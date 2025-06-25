package dev.uncandango.alltheleaks.fix.common.mods.kubejs;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(issueId = "commit_b8d4311", modId = "kubejs", versionRange = "[2101.7.1-build.181,2101.7.1-build.187]", mixins = "main.CachedTagLookupMixin", onlyIfModAbsent = "kubejstweaks")
public class FixCachedTagLookup {
}
