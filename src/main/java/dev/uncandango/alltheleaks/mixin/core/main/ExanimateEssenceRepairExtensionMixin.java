package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.compat.jei.extension.ExanimateEssenceRepairExtension;

import java.util.stream.Stream;

@Mixin(ExanimateEssenceRepairExtension.class)
public class ExanimateEssenceRepairExtensionMixin {
	@CompatibleHashes(values = {1744421922})
	@ModifyExpressionValue(method = "setRecipe", at = @At(value = "INVOKE", target = "Ljava/util/Arrays;stream([Ljava/lang/Object;)Ljava/util/stream/Stream;"))
	private Stream<ItemStack> atl$makeAcopy(Stream<ItemStack> original){
		return original.map(ItemStack::copy);
	}
}
