package dev.uncandango.alltheleaks.leaks.common.mods.forge;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.server.ServerStoppedEvent;

import java.lang.invoke.VarHandle;

@Issue(modId = "forge", versionRange = "[47.2,)")
public class UntrackedIssue002 {
	public static final VarHandle DUMMY_CONNECTION;
	public static final VarHandle PACKET_LISTENER;

	static {
		DUMMY_CONNECTION = ReflectionHelper.getFieldFromClass(FakePlayer.FakePlayerNetHandler.class, "DUMMY_CONNECTION", Connection.class, true);
		PACKET_LISTENER = ReflectionHelper.getFieldFromClass(Connection.class, "packetListener", PacketListener.class, false);
	}

	public UntrackedIssue002() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearPacketListener);
	}

	private void clearPacketListener(ServerStoppedEvent event) {
		PACKET_LISTENER.set(DUMMY_CONNECTION.get(), (Object) null);
	}
}
