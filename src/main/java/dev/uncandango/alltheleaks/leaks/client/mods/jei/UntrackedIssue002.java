package dev.uncandango.alltheleaks.leaks.client.mods.jei;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "jei", versionRange = "[15.4,15.5)", mixins = {"main.RecipesGuiMixin"},
description = "Clears `RecipesGui#recipeTransferButtons` on client player update")
public class UntrackedIssue002 {
}
