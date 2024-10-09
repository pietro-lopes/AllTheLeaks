package dev.uncandango.alltheleaks.mixin.core.main;

import com.minecolonies.api.colony.jobs.IJob;
import com.minecolonies.api.entity.ModEntities;
import com.minecolonies.core.colony.CitizenData;
import com.minecolonies.core.compatibility.jei.JobBasedRecipeCategory;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = JobBasedRecipeCategory.class, remap = false)
public class JobBasedRecipeCategoryMixin implements UpdateableLevel<JobBasedRecipeCategory<?>> {
	@Mutable
	@Shadow
	@Final
	private @NotNull EntityCitizen citizen;

	@Shadow
	@Final
	@NotNull
	protected IJob<?> job;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void grabInstance(IJob job, RecipeType type, ItemStack icon, IGuiHelper guiHelper, CallbackInfo ci){
		UpdateableLevel.register(this);
	}

	@Override
	public void onClientLevelUpdated(ClientLevel level) {
		this.citizen = createCitizenWithJob(this.job, level);
	}

	@Unique
	private EntityCitizen createCitizenWithJob(IJob<?> job, Level level) {
		EntityCitizen citizen = new EntityCitizen(ModEntities.CITIZEN, level);
		citizen.setFemale(citizen.getRandom().nextBoolean());
		citizen.setTextureId(citizen.getRandom().nextInt(255));
		citizen.getEntityData().set(EntityCitizen.DATA_TEXTURE_SUFFIX, CitizenData.SUFFIXES.get(citizen.getRandom().nextInt(CitizenData.SUFFIXES.size())));
		citizen.setModelId(job.getModel());
		return citizen;
	}
}
