package dev.uncandango.alltheleaks.leaks.common.mods.neoforge;

import com.google.common.collect.Maps;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.PlayerAdvancementsAccessor;
import net.minecraft.server.PlayerAdvancements;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

import java.util.Map;
import java.util.UUID;

@Issue(issueId = "#1487", modId = "neoforge", versionRange = "[21.,)", mixins = {"main.ServerPlayerMixin", "main.PlayerListMixin","accessor.PlayerAdvancementsAccessor"})
public class Issue1487 {
	public static final Map<UUID, PlayerAdvancements> atl$fakeAdvancements = Maps.newHashMap();

	public Issue1487() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearOnLevelUnload);
		gameBus.addListener(this::clearOnServerStopped);
	}

	private void clearOnLevelUnload(LevelEvent.Unload event) {
		if (!event.getLevel().isClientSide()){
			atl$fakeAdvancements.entrySet().removeIf(entry -> entry.getValue() != null && entry.getValue() instanceof PlayerAdvancementsAccessor accessor && accessor.getPlayer().level() == event.getLevel());
		}
	}

	private void clearOnServerStopped(ServerStoppedEvent event) {
		atl$fakeAdvancements.clear();
	}
}
