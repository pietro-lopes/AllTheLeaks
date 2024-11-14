package dev.uncandango.alltheleaks.diag.common.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "minecraft", versionRange = "1.21.1", mixins = {"main.ClientboundSetPlayerTeamPacketMixin"}, config = "scoreboardDebug")
public class ScoreboardDebug {
}
