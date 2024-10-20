package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStack.class)
public class ItemStackMixin {

	@WrapMethod(method = "setEntityRepresentation")
	private void atl$checkEmpty(Entity entity, Operation<Void> original) {
		if (!((ItemStack) (Object) this).isEmpty()) {
			original.call(entity);
		}
	}
}
