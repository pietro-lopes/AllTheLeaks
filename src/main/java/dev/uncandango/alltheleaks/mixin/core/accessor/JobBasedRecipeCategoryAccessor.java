package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.minecolonies.api.colony.jobs.IJob;
import com.minecolonies.api.entity.ModEntities;
import com.minecolonies.core.colony.CitizenData;
import com.minecolonies.core.compatibility.jei.JobBasedRecipeCategory;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Pseudo
@Mixin(JobBasedRecipeCategory.class)
public interface JobBasedRecipeCategoryAccessor {
	@Accessor("job")
	IJob<?> getJob();

	@Mutable
	@Accessor("citizen")
	void setCitizen(EntityCitizen citizen);
}
