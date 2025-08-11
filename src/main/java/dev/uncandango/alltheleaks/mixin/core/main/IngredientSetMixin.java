package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.Lockable;
import mezz.jei.library.ingredients.IngredientSet;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = IngredientSet.class, remap = false)
public class IngredientSetMixin<V> {

	@ModifyArg(method = "add", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), index = 1)
	private Object atl$copyLocked(Object value){
		if (value instanceof ItemStack stack){
			if ((Object)stack instanceof Lockable lockable && lockable.atl$isLocked()) {
				return stack.copy();
			}
		}
		return value;
	}
}
