package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.blay09.mods.cookingforblockheads.crafting.CraftingOperation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.stream.Stream;

@Mixin(CraftingOperation.class)
public class CraftingOperationMixin {
	@CompatibleHashes(values = {1104021346})
	@ModifyExpressionValue(method = "getIngredientOptions(Lnet/minecraft/world/item/crafting/Ingredient;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;getItems()[Lnet/minecraft/world/item/ItemStack;"))
	private ItemStack[] safeCopyStack(ItemStack[] original){
		return Stream.of(original).map(ItemStack::copy).toArray(ItemStack[]::new);
	}
}
