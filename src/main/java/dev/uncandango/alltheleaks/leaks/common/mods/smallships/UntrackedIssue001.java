package dev.uncandango.alltheleaks.leaks.common.mods.smallships;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;

import java.lang.invoke.VarHandle;
import java.util.List;

@Issue(
	modId = "smallships", versionRange = "[2.0.0-b1.4,)", mixins = "main.ChunkMapMixin",
	description = "Clears Smallships mixin variables `ChunkMap#serverPlayer/list` after usage")
public class UntrackedIssue001 {
	private static final VarHandle SERVER_PLAYER_HANDLE;
	private static final VarHandle LIST_HANDLE;

	static {
		SERVER_PLAYER_HANDLE = ReflectionHelper.getFieldFromClass(ChunkMap.class, "serverPlayer", ServerPlayer.class, false);
		LIST_HANDLE = ReflectionHelper.getFieldFromClass(ChunkMap.class, "list", List.class, false);
	}

	public static void clearSmallshipsVariables(ChunkMap chunkMap) {
		SERVER_PLAYER_HANDLE.set(chunkMap, null);
		((List<?>) LIST_HANDLE.get(chunkMap)).clear();
	}
}
