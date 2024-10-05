package dev.uncandango.alltheleaks.leaks.common.mods.journeymap;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.OnCloneEvent;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import journeymap.common.nbt.PlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "journeymap", versionRange = "[1.21-6.0.0-beta.26,)", mixins = {"main.PlayerDataMixin"})
public class UntrackedIssue001 {
//	private static final VarHandle PLAYER_MAP;
//	private static final VarHandle DATA;
//	private static final VarHandle PLAYER;

	public static ServerPlayer clonedPlayer;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearPlayerOnClone);
	}

	private void clearPlayerOnClone(PlayerEvent.Clone event){
		var pData = PlayerData.getPlayerData();
		if (pData instanceof OnCloneEvent accessor){
			clonedPlayer = (ServerPlayer)event.getEntity();
			accessor.atl$onCloneEvent(event);
			clonedPlayer = null;
		}
	}

//	static {
//		PLAYER_MAP = ReflectionHelper.getFieldFromClass(PlayerData.class, "playerMap", Map.class, false);
//		DATA = ReflectionHelper.getFieldFromClass(PlayerData.class, "data", CompoundTag.class, false);
//		PLAYER = ReflectionHelper.getFieldFromClass(PlayerData.Player.class, "player", ServerPlayer.class, false);
//	}
}
