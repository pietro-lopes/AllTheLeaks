package dev.uncandango.alltheleaks.feature.common.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Issue(modId = "minecraft", issueId = "Ingredient Deduplication",versionRange = "1.21.1", mixins = {"main.IngredientMixin", "main.IngredientMixin$IngredientAccessor"}, config = "ingredientDedupe", configActivated = false)
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
