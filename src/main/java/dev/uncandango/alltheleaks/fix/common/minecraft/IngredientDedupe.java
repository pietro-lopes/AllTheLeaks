package dev.uncandango.alltheleaks.fix.common.minecraft;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.TimeUnit;

@Issue(modId = "minecraft", versionRange = "[1.21.1,)", mixins = {"main.IngredientMixin", "main.IngredientMixin$IngredientAccessor"}, propertyFlag = "alltheleaks.dedupe.ingredients")
public class IngredientDedupe {
	public static final ThreadLocal<Cache<Ingredient, Ingredient>> INGREDIENT_CACHE = ThreadLocal.withInitial(() -> CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).build());

	public IngredientDedupe() {
		AllTheLeaks.LOGGER.warn("EXPERIMENTAL FEATURE ENABLED: \"INGREDIENTS DEDUPE\"");
	}

}
