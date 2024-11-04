package dev.uncandango.alltheleaks.leaks.client.mods.jeresources;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "jeresources", versionRange = "[1.4.0.247,)", mixins = {"main.MobTableBuilderMixin", "main.MobEntryMixin","main.AbstractVillagerEntryMixin"},
description = "Uses `Minecraft#level` to create entities on `MobTableBuilder#add/addSheep` and updates `MobTableBuilder#level` on level update")
public class UntrackedIssue001 {
}
