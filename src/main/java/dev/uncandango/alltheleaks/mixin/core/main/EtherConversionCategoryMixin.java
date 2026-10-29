package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;

@Mixin(targets = {"cn.leolezury.eternalstarlight.common.compat.jei.category.EtherConversionCategory"})
public class EtherConversionCategoryMixin {
	@CompatibleHashes(values = {254821593})
	@ModifyExpressionValue(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lnet/minecraft/world/item/crafting/RecipeHolder;Lmezz/jei/api/recipe/IFocusGroup;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;getItems()[Lnet/minecraft/world/item/ItemStack;"))
	private ItemStack[] atl$safeCopyStacks(ItemStack[] original){
		return Arrays.stream(original).map(ItemStack::copy).toArray(ItemStack[]::new);
	}
}
