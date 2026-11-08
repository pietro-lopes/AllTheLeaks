package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiPredicate;

@Mixin(value = Entity.class, remap = false)
public class EntityMixin2 {

	@ModifyExpressionValue(method = "isInFluidType(Ljava/util/function/BiPredicate;Z)Z", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2DoubleMap;object2DoubleEntrySet()Lit/unimi/dsi/fastutil/objects/ObjectSet;", ordinal = 0))
	private ObjectSet<Object2DoubleMap.Entry<FluidType>> atl$allMatchStream(ObjectSet<Object2DoubleMap.Entry<FluidType>> original, @Local(argsOnly = true) BiPredicate<FluidType, Double> predicate, @Cancellable CallbackInfoReturnable<Boolean> cir){
		var it = ((Object2DoubleMap.FastEntrySet<FluidType>)original).fastIterator();
		while (it.hasNext()) {
			var entry = it.next();
			if (!predicate.test(entry.getKey(), entry.getDoubleValue())) {
				cir.setReturnValue(false);
				return original;
			}
		}
		cir.setReturnValue(true);
		return original;
	}

	@ModifyExpressionValue(method = "isInFluidType(Ljava/util/function/BiPredicate;Z)Z", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2DoubleMap;object2DoubleEntrySet()Lit/unimi/dsi/fastutil/objects/ObjectSet;", ordinal = 1))
	private ObjectSet<Object2DoubleMap.Entry<FluidType>> atl$anyMatchStream(ObjectSet<Object2DoubleMap.Entry<FluidType>> original, @Local(argsOnly = true) BiPredicate<FluidType, Double> predicate, @Cancellable CallbackInfoReturnable<Boolean> cir){
		var it = ((Object2DoubleMap.FastEntrySet<FluidType>)original).fastIterator();
		while (it.hasNext()) {
			var entry = it.next();
			if (predicate.test(entry.getKey(), entry.getDoubleValue())) {
				cir.setReturnValue(true);
				return original;
			}
		}
		cir.setReturnValue(false);
		return original;
	}
}
