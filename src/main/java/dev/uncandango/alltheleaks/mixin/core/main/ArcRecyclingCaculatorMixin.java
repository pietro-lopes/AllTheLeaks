package dev.uncandango.alltheleaks.mixin.core.main;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.common.crafting.ArcRecyclingCalculator;
import dev.uncandango.alltheleaks.mixin.ArcRecyclingCalculatorExtension;
import org.apache.commons.lang3.mutable.Mutable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Pseudo
@Mixin(value = ArcRecyclingCalculator.class, remap = false)
public abstract class ArcRecyclingCaculatorMixin implements ArcRecyclingCalculatorExtension {

    @Inject(method = "makeFuture", at = @At(value = "RETURN"))
    private static void atl$testing(CallbackInfoReturnable<Mutable<List<ArcFurnaceRecipe>>> cir) {
        atl$recipes.setValue(cir.getReturnValue());
    }

}
