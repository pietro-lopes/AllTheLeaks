package dev.uncandango.alltheleaks.leaks.common.mods.forge;

import com.google.common.collect.Maps;
import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraft.server.PlayerAdvancements;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppedEvent;

import java.util.Map;
import java.util.UUID;

@Issue(modId = "forge", versionRange = "[47.2,)", mixins = {"main.ServerPlayerMixin", "main.PlayerListMixin"},
	description = "Make FakePlayers stop listening to advancements")
public class UntrackedIssue001 {

	public static final Map<UUID, PlayerAdvancements> atl$fakeAdvancements = Maps.newHashMap();

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearOnServerStopped);
	}

	private void clearOnServerStopped(ServerStoppedEvent event) {
		atl$fakeAdvancements.clear();
	}
}
