package dev.uncandango.alltheleaks.leaks.client.mods.badpackets;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import lol.bai.badpackets.impl.handler.ClientPlayPacketHandler;
import net.minecraft.client.multiplayer.ClientPacketListener;

@Issue(modId = "badpackets", versionRange = "0.8.1", description = "Clears `ChannelRegistry#handlers` map on packet close", mixins = "main.ClientPacketListenerMixin")
public class UntrackedIssue001 {

	public static void clearPacketsFromHandler(ClientPacketListener listener) {
		try {
			if (listener instanceof ClientPlayPacketHandler.Holder holder) {
				holder.badpackets_getHandler().remove();
			}
		} catch (Throwable e) {
			AllTheLeaks.LOGGER.error("Error while trying to clear packets from handler", e);
		}
	}
}
