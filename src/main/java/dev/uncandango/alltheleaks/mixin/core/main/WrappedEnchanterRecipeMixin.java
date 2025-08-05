package dev.uncandango.alltheleaks.mixin.core.main;

import com.enderio.machines.common.integrations.jei.util.WrappedEnchanterRecipe;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;

@Mixin(value = WrappedEnchanterRecipe.class)
public class WrappedEnchanterRecipeMixin {
	@CompatibleHashes(values = {1337276878})
	@ModifyExpressionValue(method = "getLapis", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;getItems()[Lnet/minecraft/world/item/ItemStack;"))
	private ItemStack[] atl$safeStack(ItemStack[] original){
		return Arrays.stream(original).map(ItemStack::copy).toArray(ItemStack[]::new);
	}
}
