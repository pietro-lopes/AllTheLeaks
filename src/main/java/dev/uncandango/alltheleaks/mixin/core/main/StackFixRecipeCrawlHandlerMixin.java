package dev.uncandango.alltheleaks.mixin.core.main;

import com.google.common.collect.Multimap;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.violetmoon.zeta.util.handler.RecipeCrawlHandler;

@Mixin(value = RecipeCrawlHandler.class, remap = false)
public class StackFixRecipeCrawlHandlerMixin {

	@Shadow
	@Final
	private static Multimap<Item, ItemStack> vanillaRecipeDigestion;

	@Shadow
	@Final
	private static Multimap<Item, ItemStack> backwardsVanillaDigestion;

	/**
	 * @author Uncandango
	 * @reason Reimplement safely
	 */
	@CompatibleHashes(values = {-1188259187})
	@Overwrite
	private static void digest(Recipe<?> recipe, RegistryAccess access) {
		ItemStack out = recipe.getResultItem(access);
		Item outItem = out.getItem();

		for(Ingredient ingredient : recipe.getIngredients()) {
			for(ItemStack inStack : ingredient.getItems()) {
				var copy = inStack.copy();
				if (copy.getCraftingRemainingItem().isEmpty()) {
					vanillaRecipeDigestion.put(copy.getItem(), out);
					backwardsVanillaDigestion.put(outItem, copy);
				}
			}
		}
	}
}
