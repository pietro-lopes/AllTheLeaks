package dev.uncandango.alltheleaks.feature.common.mods.minecraft;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Issue(modId = "minecraft", issueId = "Ingredient Deduplication" ,versionRange = "1.20.1", mixins = {"main.IngredientMixin", "main.IngredientMixin$IngredientAccessor", "main.IngredientMixin$TagValueMixin", "main.IngredientMixin$TagValueAccessor", "main.IngredientMixin$ItemValueMixin", "main.IngredientMixin$ItemValueAccessor",}, config = "ingredientDedupe", configActivated = false,
description = "Deduplicates VANILLA ingredients to reduce memory usage")
public class IngredientDedupe implements PreparableReloadListener {
	private static final ObjectOpenCustomHashSet<Ingredient> INGREDIENT_CACHE;
	public static IngredientDedupe INSTANCE;

	static {
		var BASIC_HASH_STRATEGY = new Hash.Strategy<Ingredient>() {
			@Override
			public int hashCode(Ingredient o) {
				return Objects.hashCode(o);
			}

			@Override
			public boolean equals(Ingredient a, Ingredient b) {
//				JsonElement aJson = null;
//				if (a != null) {
//					aJson = a.toJson();
//				}
//				JsonElement bJson = null;
//				if (b != null) {
//					bJson = b.toJson();
//				}
//				var objEquality = Objects.equals(a, b);
//				var jsonEquality = Objects.equals(aJson, bJson);
//				if (objEquality && jsonEquality) {
//					return true;
//				}
//				if (a != null && b != null) {
//					if (jsonEquality == false && objEquality == true) {
//						AllTheLeaks.LOGGER.warn("Ingredient: {} from class {} is not equal to: {} from class {}", a, a.getClass(), b, b.getClass());
//						AllTheLeaks.LOGGER.warn("Jsons: {} vs {}", aJson, bJson);
//					}
//					if (jsonEquality == true && objEquality == false) {
//						AllTheLeaks.LOGGER.warn("Ingredient: {} from class {} parses equally to: {} from class {}", a, a.getClass(), b, b.getClass());
//					}
//				}
//				return false;
				return Objects.equals(a, b);
			}
		};
		INGREDIENT_CACHE = new ObjectOpenCustomHashSet<>(BASIC_HASH_STRATEGY);
	}

	public static IngredientDedupe getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new IngredientDedupe();
		}
		return INSTANCE;
	}

	public synchronized static Ingredient intern(Ingredient ingredient) {
		return INGREDIENT_CACHE.addOrGet(ingredient);
	}

	@Override
	public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
		return CompletableFuture.runAsync(INGREDIENT_CACHE::clear, backgroundExecutor).thenCompose(preparationBarrier::wait);
	}

	@Override
	public String getName() {
		return "atl_ingredient_dedupe";
	}
}
