package dev.uncandango.alltheleaks.mixin.core.main;

import com.klikli_dev.theurgykubejs.TheurgyRecipeSchema;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TheurgyRecipeSchema.class)
public interface TheurgyRecipeSchemaMixin {
	@ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Ldev/latvian/mods/kubejs/recipe/component/RecipeComponentType;inputKey(Ljava/lang/String;)Ldev/latvian/mods/kubejs/recipe/RecipeKey;", ordinal = 0))
	private static RecipeKey<?> enableOptional(RecipeKey<?> original){
		return original.defaultOptional();
	}
}
