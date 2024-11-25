package dev.uncandango.alltheleaks.leaks.client.mods.nuclearcraft;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import igentuman.nc.handler.event.client.TooltipHandler;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.lang.invoke.VarHandle;

@Issue(modId = "nuclearcraft", versionRange = "[1.0.0,)", description = "Clear `TooltipHandler#processedEvent` on client player clone")
public class UntrackedIssue001 {
	public static final VarHandle PROCESSED_EVENT;

	static {
		PROCESSED_EVENT = ReflectionHelper.getFieldFromClass(TooltipHandler.class, "processedEvent", ItemTooltipEvent.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearTooltipEvent);
	}

	private void clearTooltipEvent(ClientPlayerNetworkEvent.Clone event) {
		PROCESSED_EVENT.set((Object) null);
	}
}
