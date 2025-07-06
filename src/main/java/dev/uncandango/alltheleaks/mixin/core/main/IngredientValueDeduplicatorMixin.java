package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.embeddedt.modernfix.neoforge.mixin.perf.ingredient_item_deduplication.PatchedDataComponentMapAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = {"org.embeddedt.modernfix.neoforge.recipe.IngredientValueDeduplicator$1"})
public class IngredientValueDeduplicatorMixin {
	@Definition(id = "mfix_getPrototype", method = "Lorg/embeddedt/modernfix/neoforge/mixin/perf/ingredient_item_deduplication/PatchedDataComponentMapAccessor;mfix$getPrototype()Lnet/minecraft/core/component/DataComponentMap;")
	@Definition(id = "aComps", local = @Local(type = PatchedDataComponentMapAccessor.class, ordinal = 0))
	@Definition(id = "bComps", local = @Local(type = PatchedDataComponentMapAccessor.class, ordinal = 1))
	@Expression("aComps.mfix_getPrototype() != bComps.mfix_getPrototype()")
	@ModifyExpressionValue(method = "areComponentsSame", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean useEquals(boolean original, @Local(name = "aComps") PatchedDataComponentMapAccessor aComps, @Local(name = "bComps") PatchedDataComponentMapAccessor bComps){
		return !aComps.mfix$getPrototype().equals(bComps.mfix$getPrototype());
	}
}
