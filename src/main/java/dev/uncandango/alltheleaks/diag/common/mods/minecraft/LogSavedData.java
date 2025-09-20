package dev.uncandango.alltheleaks.diag.common.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "neoforge", issueId = "Log Saved Data", versionRange = "[21.1,)", mixins = {"main.SavedDataMixin"})
public class LogSavedData {
}
