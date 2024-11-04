package dev.uncandango.alltheleaks.leaks.client.mods.jei;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "jei", versionRange = "[15.8.2.24,)", mixins = {"main.RecipeTransferButtonMixin","main.LazySortedRecipeLayoutListMixin"},
description = "Updates `RecipeTransferButton#player` and `RecipeTransferButton#parentContainer` on client player clone using `RecipeTransferButton#update()`")
public class UntrackedIssue001 {
}
