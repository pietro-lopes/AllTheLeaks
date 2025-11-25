package dev.uncandango.alltheleaks.diag.common.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "modernfix", issueId = "Adds mod aggregation on modernfix", versionRange = "*", mixins = {"main.ModernFixSparkPluginMixin"}, extraModDep = "spark", extraModDepVersions = {"*"}, devOnly = true)
public class ModernFixProfilerByMod {
}
