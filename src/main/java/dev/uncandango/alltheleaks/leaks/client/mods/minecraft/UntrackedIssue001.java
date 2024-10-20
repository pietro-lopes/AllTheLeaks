package dev.uncandango.alltheleaks.leaks.client.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.searchtree.RefreshableSearchTree;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;

@Issue(modId = "minecraft", versionRange = "[1.20.1,)", mixins = "main.ItemStackMixin")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearEntities);
		gameBus.addListener(this::clearSearchTree);
	}

	private void clearEntities(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
			Minecraft.getInstance().crosshairPickEntity = null;
			Minecraft.getInstance().hitResult = null;
//			var player = Minecraft.getInstance().player;
//			if (player != null) player.stopUsingItem();
//			Minecraft.getInstance().getEntityRenderDispatcher().crosshairPickEntity = null;
//			Minecraft.getInstance().getBlockEntityRenderDispatcher().cameraHitResult = null;
//			((ClientLevel)event.getLevel()).entitiesForRendering().forEach(entity -> {
//				entity.getEntityData().hasItem(ITEM_DATA)
//			});
		}
	}

	private void clearSearchTree(ClientPlayerNetworkEvent.LoggingOut event) {
		Minecraft.getInstance().getSearchTreeManager().register(SearchRegistry.RECIPE_COLLECTIONS, (list) -> RefreshableSearchTree.empty());
	}
}
