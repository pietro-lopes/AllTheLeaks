package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.patchouli.client.book.gui.GuiBookEntry;

import java.util.stream.Stream;

@Mixin(GuiBookEntry.class)
public class GuiBookEntryMixin {
	@CompatibleHashes(values = {-1244672177})
	@ModifyExpressionValue(method = "renderIngredient", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;getItems()[Lnet/minecraft/world/item/ItemStack;"))
	private ItemStack[] safeCopyStack(ItemStack[] original){
		return Stream.of(original).map(ItemStack::copy).toArray(ItemStack[]::new);
	}
}
