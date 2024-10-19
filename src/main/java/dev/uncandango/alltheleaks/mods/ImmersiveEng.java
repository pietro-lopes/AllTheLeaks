package dev.uncandango.alltheleaks.mods;

import blusunrize.immersiveengineering.api.crafting.*;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import blusunrize.immersiveengineering.api.energy.GeneratorFuel;
import blusunrize.immersiveengineering.api.energy.ThermoelectricSource;
import blusunrize.immersiveengineering.api.energy.WindmillBiome;
import blusunrize.immersiveengineering.api.excavator.MineralMix;
import dev.uncandango.alltheleaks.mixin.ArcRecyclingCalculatorExtension;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RecipesUpdatedEvent;

public interface ImmersiveEng {
    static void run() {
        var level = Minecraft.getInstance().level;
        CachedRecipeList.onRecipeUpdatedClient(new RecipesUpdatedEvent(level.getRecipeManager()));
        AlloyRecipe.RECIPES.getRecipes(level);
        ArcFurnaceRecipe.RECIPES.getRecipes(level);
        BlastFurnaceFuel.RECIPES.getRecipes(level);
        BlastFurnaceRecipe.RECIPES.getRecipes(level);
        BlueprintCraftingRecipe.RECIPES.getRecipes(level);
        BottlingMachineRecipe.RECIPES.getRecipes(level);
        ClocheFertilizer.RECIPES.getRecipes(level);
        ClocheRecipe.RECIPES.getRecipes(level);
        CokeOvenRecipe.RECIPES.getRecipes(level);
        CrusherRecipe.RECIPES.getRecipes(level);
        FermenterRecipe.RECIPES.getRecipes(level);
        MetalPressRecipe.STANDARD_RECIPES.getRecipes(level);
        MixerRecipe.RECIPES.getRecipes(level);
        RefineryRecipe.RECIPES.getRecipes(level);
        SawmillRecipe.RECIPES.getRecipes(level);
        SqueezerRecipe.RECIPES.getRecipes(level);
        MineralMix.RECIPES.getRecipes(level);
        GeneratorFuel.RECIPES.getRecipes(level);
        ThermoelectricSource.ALL_SOURCES.getRecipes(level);
        WindmillBiome.ALL_BIOMES.getRecipes(level);
        ArcRecyclingCalculatorExtension.atl$recipes.getValue().getValue().clear();
    }
}
