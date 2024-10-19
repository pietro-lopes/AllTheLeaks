package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.google.common.collect.Maps;
import net.minecraft.world.item.crafting.Recipe;
import org.cyclops.cyclopscore.modcompat.jei.RecipeRegistryJeiRecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Pseudo
@Mixin(value = RecipeRegistryJeiRecipeWrapper.class, remap = false)
public interface RecipeRegistryJeiRecipeWrapperAccessor {
    @Accessor("RECIPE_WRAPPERS")
    static Map<Recipe<?>, RecipeRegistryJeiRecipeWrapper<?, ?, ?>> atl$getRECIPE_WRAPPERS() {
        return Maps.newIdentityHashMap();
    }

}
