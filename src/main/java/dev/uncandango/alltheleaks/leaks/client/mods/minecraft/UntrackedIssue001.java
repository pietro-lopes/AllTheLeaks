package dev.uncandango.alltheleaks.leaks.client.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.searchtree.RefreshableSearchTree;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;

@Issue(modId = "minecraft", versionRange = "1.20.1", mixins = {"main.ATLItemStackMixin", "main.MinecraftMixin", "main.SynchedEntityDataMixin"},
description = "Prevents putting entity on constant `ItemStack.EMPTY`, clear entities on crosshair/hitresult level update, clears old `ItemStack#entityRepresentation` from tracked itemstacks at `SynchedEntityData`")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearEntities);
		// Move this to fix or feature
		//		gameBus.addListener(this::clearSearchTree);
	}

	private void clearEntities(UpdateableLevel.RenderEnginesUpdated event) {
		Minecraft.getInstance().crosshairPickEntity = null;
		Minecraft.getInstance().hitResult = null;
	}

//	private void clearSearchTree(ClientPlayerNetworkEvent.LoggingOut event) {
//		Minecraft.getInstance().getSearchTreeManager().register(SearchRegistry.RECIPE_COLLECTIONS, (list) -> RefreshableSearchTree.empty());
//	}
}
