package dev.uncandango.alltheleaks.mixin.core.accessor;

import dev.emi.emi.api.stack.EmiStack;
import fzzyhmstrs.emi_loot.emi.MobLootRecipe;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobLootRecipe.class)
public interface MobLootRecipeAccessor {
	@Accessor("type")
	EntityType<?> getType();

	@Accessor("inputStack")
	EmiStack getInputStack();
}
