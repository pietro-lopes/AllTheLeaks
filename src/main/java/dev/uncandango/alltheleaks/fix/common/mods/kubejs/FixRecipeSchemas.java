package dev.uncandango.alltheleaks.fix.common.mods.kubejs;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "kubejs", versionRange = "[2101.7.1-build.181,2101.7.1-build.187]", mixins = {"main.RecipeComponentBuilderMixin","main.RecipeComponentBuilderMixin$ValueMixin","main.RecipeComponentBuilderMixin$MapCodecMixin"})
public class FixRecipeSchemas {
}
