package dev.uncandango.alltheleaks.leaks.client.mods.emi_loot;

import dev.emi.emi.api.EmiApi;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.EntityEmiStackAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.MobLootRecipeAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import static fzzyhmstrs.emi_loot.emi.EmiClientPlugin.MOB_CATEGORY;

@Issue(modId = "emi_loot", versionRange = "[0.7.2,)", mixins = {"accessor.EntityEmiStackAccessor", "accessor.MobLootRecipeAccessor"})
public class UntrackedIssue001 {

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::updateCachedEntityFromRecipe);
	}

	private void updateCachedEntityFromRecipe(LevelEvent.Load event) {
		if (event.getLevel().isClientSide()) {
			EmiApi.getRecipeManager().getRecipes(MOB_CATEGORY).forEach(recipe -> {
				if (recipe instanceof MobLootRecipeAccessor lootRecipe) {
					Entity entity = lootRecipe.getType().create((Level) event.getLevel());
					if (lootRecipe.getInputStack() instanceof EntityEmiStackAccessor accessor) {
						accessor.setEntity(entity);
					}
				}
			});
		}
	}
}
