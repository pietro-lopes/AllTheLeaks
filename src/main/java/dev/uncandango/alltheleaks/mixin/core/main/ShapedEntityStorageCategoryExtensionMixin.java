package dev.uncandango.alltheleaks.mixin.core.main;

import com.enderio.base.common.integrations.jei.extension.ShapedEntityStorageCategoryExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;

@Pseudo
@Mixin(ShapedEntityStorageCategoryExtension.class)
public class ShapedEntityStorageCategoryExtensionMixin {
	@ModifyExpressionValue(method = {"lambda$setRecipe$2","lambda$setRecipe$4","lambda$setRecipe$6", "lambda$setRecipe$9"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;getItems()[Lnet/minecraft/world/item/ItemStack;"))
	private static ItemStack[] makeACopy(ItemStack[] original){
		return Arrays.stream(original).map(ItemStack::copy).toArray(ItemStack[]::new);
	}
}
