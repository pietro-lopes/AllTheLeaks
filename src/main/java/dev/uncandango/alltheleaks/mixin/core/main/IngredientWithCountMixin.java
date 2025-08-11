package dev.uncandango.alltheleaks.mixin.core.main;

import cofh.lib.util.crafting.IngredientWithCount;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;

@Mixin(IngredientWithCount.class)
public abstract class IngredientWithCountMixin extends AbstractIngredient {

	@Shadow(remap = false)
	@Final
	private Ingredient wrappedIngredient;
	@Shadow(remap = false)
	@Final
	private int count;
	@Unique
	private ItemStack[] atl$cachedStacks;

	/**
	 * @author Uncandango
	 * @reason Avoid editing stacks
	 */
	@CompatibleHashes(values = {-1497566620})
	@Overwrite
	public ItemStack[] getItems() {
		if (atl$cachedStacks == null) {
			atl$cachedStacks = Arrays.stream(this.wrappedIngredient.getItems()).map(stack -> stack.copyWithCount(this.count)).toArray(ItemStack[]::new);
		}
		return atl$cachedStacks;
	}

	@Override
	public void invalidate(){
		atl$cachedStacks = null;
		super.invalidate();
	}
}
