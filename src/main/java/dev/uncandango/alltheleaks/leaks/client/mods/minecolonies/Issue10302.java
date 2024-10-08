package dev.uncandango.alltheleaks.leaks.client.mods.minecolonies;

import com.minecolonies.api.colony.jobs.IJob;
import com.minecolonies.api.crafting.GenericRecipe;
import com.minecolonies.api.entity.ModEntities;
import com.minecolonies.core.colony.CitizenData;
import com.minecolonies.core.compatibility.jei.ModRecipeTypes;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.GenericRecipeAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.JobBasedRecipeCategoryAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.MineColoniesJEIPluginAccessor;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.common.Internal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.ArrayList;
import java.util.List;

@Issue(modId = "minecolonies", issueId = "#10302", versionRange = "[1.1.701-1.21.1-snapshot,1.1.724-1.21.1]", mixins = {"main.MineColoniesJEIPluginMixin", "accessor.MineColoniesJEIPluginAccessor", "accessor.JobBasedRecipeCategoryAccessor", "accessor.GenericRecipeAccessor", "main.GenericRecipeMixin"})
public class Issue10302 {
	public static final List<GenericRecipe> recipesWithEntities = new ArrayList<>();
	public static IModPlugin mineColoniesJEIPlugin;

	public Issue10302() {
		var gameBus = NeoForge.EVENT_BUS;
		if (ModList.get().isLoaded("jei")) {
			gameBus.addListener(this::refreshCitizens);
			gameBus.addListener(this::clearRecipeList);
		}
	}

	private void clearRecipeList(ClientPlayerNetworkEvent.LoggingOut event) {
		recipesWithEntities.clear();
	}

	private void refreshCitizens(LevelEvent.Load event) {
		if (event.getLevel().isClientSide()) {
			IJeiRuntime jeiRuntime;
			try {
				jeiRuntime = Internal.getJeiRuntime();
			} catch (IllegalStateException ignored) {
				return;
			}
			var list = ((MineColoniesJEIPluginAccessor) mineColoniesJEIPlugin).getCategories();
			list.forEach(category -> {
				if (category instanceof JobBasedRecipeCategoryAccessor accessor) {
					var citizen = createCitizenWithJob(accessor.getJob(), (Level) event.getLevel());
					accessor.setCitizen(citizen);
				}
			});
			var fishing = jeiRuntime.getRecipeManager().getRecipeCategory(ModRecipeTypes.FISHING);
			if (fishing instanceof JobBasedRecipeCategoryAccessor accessor) {
				var citizen = createCitizenWithJob(accessor.getJob(), (Level) event.getLevel());
				accessor.setCitizen(citizen);
			}
			recipesWithEntities.forEach(recipe -> {
				if (recipe instanceof GenericRecipeAccessor accessor) {
					var oldEntity = accessor.getRequiredEntity();
					if (oldEntity != null) {
						var newEntity = accessor.getRequiredEntity().getType().create((Level) event.getLevel());
						accessor.setRequiredEntity((LivingEntity) newEntity);
					}
				}
			});
		}
	}

	private EntityCitizen createCitizenWithJob(IJob<?> job, Level level) {
		EntityCitizen citizen = new EntityCitizen(ModEntities.CITIZEN, level);
		citizen.setFemale(citizen.getRandom().nextBoolean());
		citizen.setTextureId(citizen.getRandom().nextInt(255));
		citizen.getEntityData().set(EntityCitizen.DATA_TEXTURE_SUFFIX, CitizenData.SUFFIXES.get(citizen.getRandom().nextInt(CitizenData.SUFFIXES.size())));
		citizen.setModelId(job.getModel());
		return citizen;
	}
}
