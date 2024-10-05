package dev.uncandango.alltheleaks.mixin.core.main;

import com.hollingsworth.arsnouveau.api.registry.CasterTomeRegistry;
import com.hollingsworth.arsnouveau.common.crafting.recipes.CasterTomeData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.Supplier;

@Mixin(value = CasterTomeRegistry.class, remap = false)
public class CasterTomeRegistryMixin {
	@WrapOperation(method = "lambda$reloadTomeData$1", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
	private static <E> boolean atl$replaceWithSafeRegistry(List<Supplier<ItemStack>> instance, E e, Operation<Boolean> original, @Local(argsOnly = true) Level level, @Local RecipeHolder<CasterTomeData> tome){
		var registry = level.registryAccess();
		return instance.add(() -> ((CasterTomeData)tome.value()).getResultItem(registry));
	}
}
