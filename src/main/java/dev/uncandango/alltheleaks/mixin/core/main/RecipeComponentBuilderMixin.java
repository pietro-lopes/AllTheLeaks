package dev.uncandango.alltheleaks.mixin.core.main;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapLike;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentBuilder;
import dev.latvian.mods.kubejs.script.KubeJSContext;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.rhino.Context;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(RecipeComponentBuilder.class)
public abstract class RecipeComponentBuilderMixin implements RecipeComponent<Map<RecipeComponentBuilder.Key, RecipeComponentBuilder.Value>> {

	@Redirect(method = {"matches*","replace*","buildUniqueId*","validate*","isEmpty*"}, at = @At(value = "FIELD", target = "Ldev/latvian/mods/kubejs/recipe/component/RecipeComponentBuilder$Value;value:Ljava/lang/Object;"))
	private Object callValue(RecipeComponentBuilder.Value instance) {
		return instance.getValue();
	}

	@Override
	public Map<RecipeComponentBuilder.Key, RecipeComponentBuilder.Value> wrap(Context cx, KubeRecipe recipe, Object from) {
		if (from instanceof Map mapLike) {
			var map = MapLike.forMap(mapLike,((KubeJSContext)cx).getRegistries().java());
			return ((RecipeComponentBuilder)(Object) this).mapCodec().decode(((KubeJSContext)cx).getRegistries().java(), Cast.to(map)).getOrThrow();
		}
		return RecipeComponent.super.wrap(cx, recipe, from);
	}

	@Mixin(RecipeComponentBuilder.Value.class)
	public static class ValueMixin {
		@Shadow
		@Final
		private Object value;

		@Inject(method = "getValue", at = @At("HEAD"), cancellable = true)
		private void unwrapDataResult(CallbackInfoReturnable<Object> cir){
			if (value instanceof DataResult<?> dr) {
				if (dr.getOrThrow() instanceof Pair<?, ?> p) {
					cir.setReturnValue(p.getFirst());
				} else {
					cir.setReturnValue(dr.getOrThrow());
				}
			}
		}
	}

	@Mixin(targets = "dev.latvian.mods.kubejs.recipe.component.RecipeComponentBuilder$1")
	public static class MapCodecMixin {
		@Redirect(method = "encode*", at = @At(value = "FIELD", target = "Ldev/latvian/mods/kubejs/recipe/component/RecipeComponentBuilder$Value;value:Ljava/lang/Object;"))
		private Object callValue(RecipeComponentBuilder.Value instance) {
			return instance.getValue();
		}
	}
}
