package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.shadowsoffire.apotheosis.compat.jei.PotionCharmExtension;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;
import java.util.stream.Stream;

@Mixin(PotionCharmExtension.class)
public class PotionCharmExtensionMixin {
	@ModifyExpressionValue(method = "setRecipe", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;map(Ljava/util/function/Function;)Ljava/util/stream/Stream;", ordinal = 0))
	private Stream<ItemStack[]> copyArray(Stream<ItemStack[]> original){
		return original.map(array -> Arrays.stream(array).map(ItemStack::copy).toArray(ItemStack[]::new));
	}
}
