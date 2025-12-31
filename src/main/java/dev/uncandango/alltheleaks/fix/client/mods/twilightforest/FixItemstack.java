package dev.uncandango.alltheleaks.fix.client.mods.twilightforest;


import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "twilightforest", mixins = "main.ExanimateEssenceRepairExtensionMixin", versionRange = "[4.8,)", extraModDep = {"jei"}, extraModDepVersions = {"*"})
public class FixItemstack {
}
