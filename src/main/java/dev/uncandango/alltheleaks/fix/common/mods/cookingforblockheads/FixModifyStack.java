package dev.uncandango.alltheleaks.fix.common.mods.cookingforblockheads;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "cookingforblockheads", mixins = {"main.MilkJarItemProviderMixin","main.CraftingOperationMixin", "main.SinkItemProviderMixin"}, versionRange = "*")
public class FixModifyStack {
}
