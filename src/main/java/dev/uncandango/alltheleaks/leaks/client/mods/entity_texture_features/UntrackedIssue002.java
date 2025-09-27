package dev.uncandango.alltheleaks.leaks.client.mods.entity_texture_features;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import traben.entity_texture_features.features.ETFManager;
import traben.entity_texture_features.features.player.ETFPlayerEntity;
import traben.entity_texture_features.utils.ETFLruCache;

import java.lang.invoke.MethodType;

@Issue(modId = "entity_texture_features", versionRange = "[7.0.0,)")
public class UntrackedIssue002 {
	public UntrackedIssue002() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::updateEntityFromManager);
	}

	static {
		var dummy = ReflectionHelper.getMethodFromClass(ETFManager.class, "getInstance", MethodType.methodType(ETFManager.class), true);
		var dummy2 = ReflectionHelper.getFieldFromClass(ETFManager.class, "PLAYER_TEXTURE_MAP", ETFLruCache.class, false);
		var dummy3 = ReflectionHelper.getClass("traben.entity_texture_features.features.player.ETFPlayerTexture");
		var dummy4 = ReflectionHelper.getClass("traben.entity_texture_features.features.player.ETFPlayerEntity");
	}

	private void updateEntityFromManager(ClientPlayerNetworkEvent.Clone event) {
		var etfTexture = ETFManager.getInstance().PLAYER_TEXTURE_MAP.get(event.getOldPlayer().getUUID());
		if (etfTexture == null) return;
		if (event.getNewPlayer() instanceof ETFPlayerEntity playerEntity) {
			etfTexture.player = playerEntity;
		}
	}
}
