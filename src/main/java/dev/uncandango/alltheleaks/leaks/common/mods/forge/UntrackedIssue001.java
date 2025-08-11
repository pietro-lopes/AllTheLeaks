package dev.uncandango.alltheleaks.leaks.common.mods.forge;

import com.google.common.collect.Maps;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.PlayerAdvancementsAccessor;
import net.minecraft.server.PlayerAdvancements;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;

import java.util.Map;
import java.util.UUID;

@Issue(modId = "forge", versionRange = "[47.2,)", mixins = {"main.ServerPlayerMixin", "main.PlayerListMixin", "accessor.PlayerAdvancementsAccessor"},
	description = "Make FakePlayers stop listening to advancements")
public class UntrackedIssue001 {

	public static final Map<UUID, PlayerAdvancements> atl$fakeAdvancements = Maps.newHashMap();

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearOnServerStopped);
		gameBus.addListener(this::clearOnLevelUnload);
	}

	private void clearOnLevelUnload(LevelEvent.Unload event) {
		if (!event.getLevel().isClientSide()) {
			var it = atl$fakeAdvancements.entrySet().iterator();
			while (it.hasNext()) {
				var entry = it.next();
				if (entry.getValue() instanceof PlayerAdvancementsAccessor accessor) {
					if (accessor.getPlayer().level() == event.getLevel()) {
						var player = accessor.getPlayer();
						if (player.connection.player instanceof FakePlayer) {
							player.connection.player = null;
						}
						it.remove();
					}
				}
			}
		}
	}

	private void clearOnServerStopped(ServerStoppedEvent event) {
		atl$fakeAdvancements.clear();
	}
}
