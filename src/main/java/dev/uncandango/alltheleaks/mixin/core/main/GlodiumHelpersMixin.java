package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.ftb.mods.ftbjeiextras.modspecific.GlodiumHelpers;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;

@Mixin(GlodiumHelpers.class)
public class GlodiumHelpersMixin {

	@ModifyExpressionValue(method = "of", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;getItems()[Lnet/minecraft/world/item/ItemStack;"))
	private static ItemStack[] atl$makeAcopy(ItemStack[] original) {
		return Arrays.stream(original).map(ItemStack::copy).toArray(ItemStack[]::new);
	}
}
