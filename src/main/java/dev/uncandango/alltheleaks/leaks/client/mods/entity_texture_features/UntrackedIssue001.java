package dev.uncandango.alltheleaks.leaks.client.mods.entity_texture_features;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.common.NeoForge;
import traben.entity_texture_features.features.ETFManager;
import traben.entity_texture_features.features.player.ETFPlayerTexture;
import traben.entity_texture_features.utils.ETFEntity;
import traben.entity_texture_features.utils.ETFLruCache;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;

@Issue(modId = "entity_texture_features", versionRange = "[6.2.5,)")
public class UntrackedIssue001 {
	public static final VarHandle ETF$HELDENTITY;
	public static final VarHandle ETF$ETFPLAYERTEXTURE;
	public static final MethodHandle ETF_INSTANCE;
	public static final VarHandle PLAYER_TEXTURE_MAP;

	static {
		ETF$HELDENTITY = ReflectionHelper.getFieldFromClass(LivingEntityRenderer.class, "etf$heldEntity", ETFEntity.class, false);
		ETF$ETFPLAYERTEXTURE = ReflectionHelper.getFieldFromClass(PlayerRenderer.class, "etf$ETFPlayerTexture", ETFPlayerTexture.class, false);
		ETF_INSTANCE = ReflectionHelper.getMethodFromClass(ETFManager.class, "getInstance", MethodType.methodType(ETFManager.class), true);
		PLAYER_TEXTURE_MAP = ReflectionHelper.getFieldFromClass(ETFManager.class, "PLAYER_TEXTURE_MAP", ETFLruCache.class, false);
	}

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearCachedEntityFromRenderer);
		gameBus.addListener(this::clearTextureMap);
	}

	private void clearCachedEntityFromRenderer(RenderLivingEvent.Post<?, ?> event) {
		ETF$HELDENTITY.set(event.getRenderer(), null);
		if (event.getRenderer() instanceof PlayerRenderer) {
			ETF$ETFPLAYERTEXTURE.set(event.getRenderer(), null);
		}
	}

	private void clearTextureMap(ClientPlayerNetworkEvent.Clone event) {
		try {
			var runTime = (ETFManager) ETF_INSTANCE.invoke();
			runTime.PLAYER_TEXTURE_MAP.clear();
			// ETFManager.getInstance().PLAYER_TEXTURE_MAP.clear();
		} catch (Throwable e) {
			AllTheLeaks.LOGGER.error("Error while trying to clear texture map from ETF", e);
		}
	}
}
