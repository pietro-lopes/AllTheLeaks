package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.stream.Stream;

@Mixin(targets = "net.blay09.mods.cookingforblockheads.block.entity.MilkJarBlockEntity$MilkJarItemProvider")
public class MilkJarItemProviderMixin {
	@CompatibleHashes(values = {1355704271})
	@ModifyExpressionValue(method = "findIngredient(Lnet/minecraft/world/item/crafting/Ingredient;Ljava/util/Collection;Lnet/blay09/mods/cookingforblockheads/api/CacheHint;)Lnet/blay09/mods/cookingforblockheads/api/IngredientToken;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;getItems()[Lnet/minecraft/world/item/ItemStack;"))
	private ItemStack[] safeCopyStack(ItemStack[] original){
		return Stream.of(original).map(ItemStack::copy).toArray(ItemStack[]::new);
	}
}
