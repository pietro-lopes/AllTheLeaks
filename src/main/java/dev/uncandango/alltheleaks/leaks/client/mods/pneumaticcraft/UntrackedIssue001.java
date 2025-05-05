package dev.uncandango.alltheleaks.leaks.client.mods.pneumaticcraft;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import me.desht.pneumaticcraft.common.pneumatic_armor.CommonArmorHandler;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.util.Map;
import java.util.UUID;

@Issue(modId = "pneumaticcraft", versionRange = "[8.0.3,)")
public class UntrackedIssue001 {
	public static final MethodHandle CLEAR_REMOTE_PLAYER;
	public static final VarHandle CLIENT_HANDLER;
	public static final VarHandle PLAYER_HANDLERS;

	static {
		CLEAR_REMOTE_PLAYER = ReflectionHelper.getMethodFromClass(CommonArmorHandler.class, "clearHandlerForPlayer", MethodType.methodType(void.class, Player.class), true);
		CLIENT_HANDLER = ReflectionHelper.getFieldFromClass(CommonArmorHandler.class, "clientHandler", CommonArmorHandler.class, true);
		PLAYER_HANDLERS = ReflectionHelper.getFieldFromClass(CommonArmorHandler.class, "playerHandlers", Map.class, false);
	}

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearRemotePlayer);
		gameBus.addListener(this::clearClientPlayers);
	}

	private void clearClientPlayers(UpdateableLevel.RenderEnginesUpdated event) {
		((Map<UUID, CommonArmorHandler>)PLAYER_HANDLERS.get((CommonArmorHandler)CLIENT_HANDLER.get())).clear();
	}

	private void clearRemotePlayer(EntityLeaveLevelEvent event) {
		if (event.getEntity() instanceof RemotePlayer player) {
			try {
				CLEAR_REMOTE_PLAYER.invoke(player);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
	}
}
