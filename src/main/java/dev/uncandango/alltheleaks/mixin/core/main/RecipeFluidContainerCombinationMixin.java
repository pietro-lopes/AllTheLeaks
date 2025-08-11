package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.minecraft.world.item.ItemStack;
import org.cyclops.evilcraft.core.recipe.type.RecipeFluidContainerCombination;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RecipeFluidContainerCombination.class)
public class RecipeFluidContainerCombinationMixin {
	@CompatibleHashes(values = 1919966941)
	@ModifyReturnValue(method = "getResultItem", at = @At("RETURN"))
	private ItemStack atl$safeStack(ItemStack original){
		return original.copy();
	}
}
