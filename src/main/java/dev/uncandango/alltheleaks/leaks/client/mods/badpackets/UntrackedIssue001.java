package dev.uncandango.alltheleaks.leaks.client.mods.badpackets;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "badpackets", versionRange = "[0.4.1,)", description = "Clears `ChannelRegistry#handlers` map on packet close", mixins = "main.ClientPacketListenerMixin")
public class UntrackedIssue001 {
}
