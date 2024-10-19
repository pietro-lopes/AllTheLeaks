package dev.uncandango.alltheleaks.mixin;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.List;

public interface ArcRecyclingCalculatorExtension {

    MutableObject<Mutable<List<ArcFurnaceRecipe>>> atl$recipes = new MutableObject<>();
}
