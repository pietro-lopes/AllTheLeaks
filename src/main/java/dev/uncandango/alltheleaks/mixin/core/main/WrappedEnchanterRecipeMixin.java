package dev.uncandango.alltheleaks.mixin.core.main;

import com.enderio.machines.common.blocks.enchanter.EnchanterRecipe;
import com.enderio.machines.common.integrations.jei.util.WrappedEnchanterRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;
import java.util.stream.Stream;

@Mixin(WrappedEnchanterRecipe.class)
public class WrappedEnchanterRecipeMixin {
	@Shadow
	@Final
	private RecipeHolder<EnchanterRecipe> recipe;

	@Shadow
	@Final
	private int level;

	@Redirect(method = "getLapis", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;peek(Ljava/util/function/Consumer;)Ljava/util/stream/Stream;"))
	private Stream<ItemStack> useCopyWithCount(Stream<ItemStack> instance, Consumer<ItemStack> consumer){
		return instance.map(stack -> stack.copyWithCount(((EnchanterRecipe)this.recipe.value()).getLapisForLevel(this.level)));
	}
}
