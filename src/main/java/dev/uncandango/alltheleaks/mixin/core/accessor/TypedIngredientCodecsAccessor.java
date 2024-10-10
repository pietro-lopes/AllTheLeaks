package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.common.codecs.TypedIngredientCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = TypedIngredientCodecs.class, remap = false)
public interface TypedIngredientCodecsAccessor {
	@Accessor("ingredientCodec")
	static void atl$setIngredientCodec(MapCodec<?> ingredientCodec){};

	@Accessor("ingredientTypeCodec")
	static void atl$setIngredientTypeCodec(Codec<?> ingredientTypeCodec){};

	@Accessor("codecMapCache")
	static Map<IIngredientType<?>, Codec<ITypedIngredient<?>>> atl$getCodecMapCache() { throw new AssertionError(); }
}
