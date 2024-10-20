package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.sugar.Local;
import net.dries007.tfc.common.capabilities.food.TFCFoodData;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(value = TFCFoodData.class, remap = false)
public class TFCFoodDataMixin {
    @ModifyArg(method = "restoreFoodStatsAfterDeath", at = @At(value = "INVOKE", target = "Lnet/dries007/tfc/common/capabilities/food/TFCFoodData;<init>(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/food/FoodData;Lnet/dries007/tfc/common/capabilities/food/NutritionData;)V"), index = 0)
    static private Player atl$fixPlayer(Player sourcePlayer, @Local(ordinal = 1) Player newPlayer){
        return newPlayer;
    }
}
