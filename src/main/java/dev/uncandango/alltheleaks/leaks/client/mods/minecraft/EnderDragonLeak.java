package dev.uncandango.alltheleaks.leaks.client.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "minecraft", versionRange = "1.20.1", mixins = {"main.EnderDragonRendererMixin", "accessor.EnderDragonRendererModelAccessor"}, onlyIfModAbsent = "modernfix",
description = "Clears Ender Dragon entity from model after usage (disabled when modernfix is loaded)")
public class EnderDragonLeak {
}
