package dev.uncandango.alltheleaks.leaks.common.mods.shulkerboxtooltip;

import com.misterpemodder.shulkerboxtooltip.impl.network.ProtocolVersion;
import com.misterpemodder.shulkerboxtooltip.impl.network.ServerNetworking;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "shulkerboxtooltip", versionRange = "*")
public class UntrackedIssue001 {
	public static final VarHandle CLIENTS;

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::updateListeners);
	}

	static {
		CLIENTS = ReflectionHelper.getFieldFromClass(ServerNetworking.class, "CLIENTS", Map.class, true);
		var dummy = ReflectionHelper.getMethodFromClass(ServerNetworking.class, "removeClient", MethodType.methodType(void.class, ServerPlayer.class), true);
		var dummy2 = ReflectionHelper.getMethodFromClass(ServerNetworking.class, "addClient", MethodType.methodType(void.class, ServerPlayer.class, ProtocolVersion.class), true);
	}

	private void updateListeners(PlayerEvent.Clone event) {
		var clients = ((Map<ServerPlayer, ProtocolVersion>)CLIENTS.get());
		var protocol = clients.get(event.getOriginal());
		if (protocol == null) return;
		if (event.getOriginal() instanceof ServerPlayer sp) {
			ServerNetworking.removeClient(sp);
		}
		if (event.getEntity() instanceof ServerPlayer sp) {
			ServerNetworking.addClient(sp, protocol);
		}

	}
}
