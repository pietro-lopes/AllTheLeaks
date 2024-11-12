package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.ftb.mods.ftbjeiextras.modspecific.GlodiumHelpers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.stream.Streams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GlodiumHelpers.class)
public class GlodiumHelpersMixin {

	@WrapOperation(method = "of", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;getItems()[Lnet/minecraft/world/item/ItemStack;"))
	private static ItemStack[] atl$makeAcopy(Ingredient instance, Operation<ItemStack[]> original){
		return Streams.of(original.call(instance)).map(ItemStack::copy).toArray(ItemStack[]::new);
	}
}
