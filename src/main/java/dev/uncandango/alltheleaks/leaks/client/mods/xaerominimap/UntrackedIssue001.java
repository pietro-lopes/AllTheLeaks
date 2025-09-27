package dev.uncandango.alltheleaks.leaks.client.mods.xaerominimap;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import xaero.common.XaeroMinimapSession;

import java.lang.invoke.VarHandle;

@Issue(modId = "xaerominimap", versionRange = "*")
public class UntrackedIssue001 {
	public static final VarHandle XAERO_MINIMAP_SESSION;

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearLastRenderedEntity);
	}

	static {
		XAERO_MINIMAP_SESSION = ReflectionHelper.getFieldFromClass(ClientPacketListener.class, "xaero_minimapSession", XaeroMinimapSession.class, false);
	}

	@SuppressWarnings("deprecation")
	private void clearLastRenderedEntity(ClientPlayerNetworkEvent.Clone event) {
		var listener = Minecraft.getInstance().getConnection();
		try {
			var session = (XaeroMinimapSession) XAERO_MINIMAP_SESSION.get(listener);
			session.getWaypointsManager().getRadarSession().getStateUpdater().setLastRenderViewEntity(null);
		} catch (Throwable e) {
			AllTheLeaks.LOGGER.error("Error while trying to clear entity from Xaero Radar", e);
		}
	}
}
