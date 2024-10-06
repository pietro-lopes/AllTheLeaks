package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.minecolonies.api.crafting.GenericRecipe;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GenericRecipe.class)
public interface GenericRecipeAccessor {
	@Mutable
	@Accessor("requiredEntity")
	void setRequiredEntity(LivingEntity entity);

	@Accessor("requiredEntity")
	LivingEntity getRequiredEntity();
}
