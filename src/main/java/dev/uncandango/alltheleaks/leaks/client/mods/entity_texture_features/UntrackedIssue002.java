package dev.uncandango.alltheleaks.leaks.client.mods.entity_texture_features;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import traben.entity_texture_features.features.ETFManager;
import traben.entity_texture_features.features.player.ETFPlayerEntity;
import traben.entity_texture_features.features.player.ETFPlayerTexture;
import traben.entity_texture_features.utils.ETFLruCache;

import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;

@Issue(modId = "entity_texture_features", versionRange = "[7.0.0,)", description = "Updates player at `ETFManager#PLAYER_TEXTURE_MAP`")
public class UntrackedIssue002 {
	public static final VarHandle etf$ETFPlayerTexture;

	public UntrackedIssue002() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::updateEntityFromManager);
		gameBus.addListener(this::clearMapOnLoggout);
	}

	private void clearMapOnLoggout(ClientPlayerNetworkEvent.LoggingOut event) {
		if (event.getPlayer() == null) return;
		var renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(event.getPlayer());
		etf$ETFPlayerTexture.set(renderer, (Object)null);
		ETFManager.getInstance().PLAYER_TEXTURE_MAP.clear();
	}

	static {
		var dummy = ReflectionHelper.getMethodFromClass(ETFManager.class, "getInstance", MethodType.methodType(ETFManager.class), true);
		var dummy2 = ReflectionHelper.getFieldFromClass(ETFManager.class, "PLAYER_TEXTURE_MAP", ETFLruCache.class, false);
		var dummy3 = ReflectionHelper.getClass("traben.entity_texture_features.features.player.ETFPlayerTexture");
		var dummy4 = ReflectionHelper.getClass("traben.entity_texture_features.features.player.ETFPlayerEntity");
		etf$ETFPlayerTexture = ReflectionHelper.getFieldFromClass(PlayerRenderer.class, "etf$ETFPlayerTexture", ETFPlayerTexture.class, false);
	}

	private void updateEntityFromManager(ClientPlayerNetworkEvent.Clone event) {
		var etfTexture = ETFManager.getInstance().PLAYER_TEXTURE_MAP.get(event.getOldPlayer().getUUID());
		if (etfTexture == null) return;
		if (event.getNewPlayer() instanceof ETFPlayerEntity playerEntity) {
			etfTexture.player = playerEntity;
		}
	}
}
