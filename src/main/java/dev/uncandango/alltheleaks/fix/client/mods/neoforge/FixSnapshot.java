package dev.uncandango.alltheleaks.fix.client.mods.neoforge;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.Connection;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.RegistryManager;

import java.lang.invoke.VarHandle;

@Issue(issueId = "#1932", modId = "neoforge", versionRange = "[21.,)")
public class FixSnapshot {
	private static final VarHandle DISCONNECTION_HANDLED;

	static {
		DISCONNECTION_HANDLED = ReflectionHelper.getFieldFromClass(Connection.class, "disconnectionHandled", boolean.class, false);
	}

	public FixSnapshot() {
		var gamebus = NeoForge.EVENT_BUS;
		gamebus.addListener(this::callRevertToFrozenOnDisconnect);
	}

	private void callRevertToFrozenOnDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
		if (Minecraft.getInstance().getConnection() instanceof ClientPacketListener listener) {
			var handled = (boolean)DISCONNECTION_HANDLED.get(listener.getConnection());
			if (!listener.getConnection().isMemoryConnection() && !handled) {
				RegistryManager.revertToFrozen();
			}
		}
	}
}
