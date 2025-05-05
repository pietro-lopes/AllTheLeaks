package dev.uncandango.alltheleaks.feature.common.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "minecraft", issueId = "Disable Search Tree", versionRange = "1.21.1", mixins = {"main.ClientPacketListenerMixin2"}, config = "disableSearchTree", configActivated = false)
public class DisableSearchTree {
}
