package dev.uncandango.alltheleaks.leaks.client.mods.entity_texture_features;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;
import traben.entity_texture_features.features.ETFManager;
import traben.entity_texture_features.features.player.ETFPlayerEntity;
import traben.entity_texture_features.utils.ETFEntity;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "entity_texture_features", versionRange = "[6.2.3,7.0.0)", mixins = "main.ATLLivingEntityRendererMixin", description = "Clears `LivingEntityRenderer#etf$heldEntity` when level changes and updates player at `ETFManager#PLAYER_TEXTURE_MAP`")
public class UntrackedIssue001 {
	public static final VarHandle ETF$HELDENTITY;
	public static final MethodHandle ETF$GET_WORLD;

	static {
		ETF$HELDENTITY = ReflectionHelper.getFieldFromClass(LivingEntityRenderer.class, "etf$heldEntity", ETFEntity.class, false);
		if (!(ETFManager.getInstance().PLAYER_TEXTURE_MAP instanceof Map)) {
			throw new IllegalArgumentException();
		}
		ReflectionHelper.getClass(ETFPlayerEntity.class.getName());
		ETF$GET_WORLD = ReflectionHelper.getMethodFromClass(ETFEntity.class, "etf$getWorld", MethodType.methodType(Level.class), false);
		var enabled = ReflectionHelper.getFieldFromClass(LivingEntityRenderer.class, "atl$etfEnabled", boolean.class, true);
		enabled.set(true);
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(UntrackedIssue001::updateEntityFromManager);
	}

	private static void updateEntityFromManager(ClientPlayerNetworkEvent.Clone event) {
		var etfTexture = ETFManager.getInstance().PLAYER_TEXTURE_MAP.get(event.getOldPlayer().getUUID());
		if (etfTexture == null) return;
		if (event.getNewPlayer() instanceof ETFPlayerEntity playerEntity) {
			etfTexture.player = playerEntity;
		}
	}

	public static void clearCachedEntityFromRenderer(LivingEntityRenderer<?,?> livingEntityRenderer, @Nullable ClientLevel level) {
		var etfEntity = (ETFEntity) ETF$HELDENTITY.get(livingEntityRenderer);
		if (etfEntity == null) return;
		var world = etfEntity.etf$getWorld();
		if (world == null || world == level) return;
		ETF$HELDENTITY.set(livingEntityRenderer, null);
	}
}
