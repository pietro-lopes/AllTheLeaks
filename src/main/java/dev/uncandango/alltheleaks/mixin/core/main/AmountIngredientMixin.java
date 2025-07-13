package dev.uncandango.alltheleaks.mixin.core.main;

import net.minecraft.world.item.ItemStack;
import org.confluence.lib.common.recipe.AmountIngredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;
import java.util.stream.Stream;

@Mixin(AmountIngredient.class)
public abstract class AmountIngredientMixin {
	@Shadow
	@Final
	private int amount;

	@Redirect(method = "getItems", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;peek(Ljava/util/function/Consumer;)Ljava/util/stream/Stream;"))
	private Stream<ItemStack> useMapNotPeek(Stream<ItemStack> instance, Consumer<ItemStack> consumer){
		return instance.map(stack -> stack.copyWithCount(this.amount));
	}
}
