package dev.uncandango.alltheleaks.leaks.client.mods.entity_texture_features;

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

import java.lang.invoke.VarHandle;

@Issue(modId = "entity_texture_features", versionRange = "[6.2.5,)")
public class UntrackedIssue001 {
	public static final VarHandle ETF$HELDENTITY;
	public static final VarHandle ETF$ETFPLAYERTEXTURE;

	static {
		ETF$HELDENTITY = ReflectionHelper.getFieldFromClass(LivingEntityRenderer.class, "etf$heldEntity", ETFEntity.class, false);
		ETF$ETFPLAYERTEXTURE = ReflectionHelper.getFieldFromClass(PlayerRenderer.class, "etf$ETFPlayerTexture", ETFPlayerTexture.class, false);
	}

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearCachedEntityFromRenderer);
		gameBus.addListener(this::clearTextureMap);
	}

	private void clearCachedEntityFromRenderer(RenderLivingEvent.Post<?, ?> event) {
		ETF$HELDENTITY.set(event.getRenderer(), null);
		if (event.getRenderer() instanceof PlayerRenderer){
			ETF$ETFPLAYERTEXTURE.set(event.getRenderer(), null);
		}
	}

	private void clearTextureMap(ClientPlayerNetworkEvent.Clone event) {
		ETFManager.getInstance().PLAYER_TEXTURE_MAP.clear();
	}
}
