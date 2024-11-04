package dev.uncandango.alltheleaks.mixin.core.main;

import com.minecolonies.api.colony.jobs.IJob;
import com.minecolonies.core.compatibility.jei.JobBasedRecipeCategory;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = JobBasedRecipeCategory.class, remap = false)
public abstract class JobBasedRecipeCategoryMixin implements UpdateableLevel<JobBasedRecipeCategory<?>> {

	@Shadow
	@Mutable
	@Final
	private @NotNull EntityCitizen citizen;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void registerInstance(CallbackInfo ci) {
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		if (level != null && this.citizen.level() != level) {
			this.citizen = JobBasedRecipeCategoryAccessor.createCitizenWithJob(this.getJob());
		}
	}

	@Shadow
	public abstract @NotNull IJob<?> getJob();

	@Mixin(value = JobBasedRecipeCategory.class, remap = false)
	public interface JobBasedRecipeCategoryAccessor {
		@Invoker("createCitizenWithJob")
		static EntityCitizen createCitizenWithJob(IJob<?> job) {
			throw new AssertionError();
		}
	}
}
