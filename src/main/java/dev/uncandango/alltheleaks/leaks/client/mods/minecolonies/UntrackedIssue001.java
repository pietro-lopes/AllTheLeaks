package dev.uncandango.alltheleaks.leaks.client.mods.minecolonies;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "minecolonies", versionRange = "[1.20.1-1.1.647-beta,1.20.1-1.1.778-beta]", mixins = {"main.JobBasedRecipeCategoryMixin","main.JobBasedRecipeCategoryMixin$JobBasedRecipeCategoryAccessor"},
	description = "Recreates citizens and entities on level update at `JobBasedRecipeCategory`")
public class UntrackedIssue001 {
}
