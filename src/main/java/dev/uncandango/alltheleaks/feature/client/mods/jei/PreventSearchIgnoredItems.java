package dev.uncandango.alltheleaks.feature.client.mods.jei;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "jei", issueId = "Prevent Search Ignored Items", versionRange = "[15.4.0.9,)", mixins = "main.ElementSearchMixin", config = "preventSearchIgnoredItems", description = "Prevents JEI from creating a search tree nodes for items that are ignored on config/jei/blacklist.cfg<br>\n*Note: This will prevent you from finding it at Creative Tab if using ModernFix search tree backed by JEI!*")
public class PreventSearchIgnoredItems {
}
