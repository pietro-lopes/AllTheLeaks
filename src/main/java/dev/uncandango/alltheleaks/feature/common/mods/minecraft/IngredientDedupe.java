package dev.uncandango.alltheleaks.feature.common.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.fml.ModList;
import org.embeddedt.modernfix.core.ModernFixMixinPlugin;

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
	public static final boolean MODERNFIX_DEDUPLICATION;

	static {
		var BASIC_HASH_STRATEGY = new Hash.Strategy<Ingredient>() {
			@Override
			public int hashCode(Ingredient o) {
				return Objects.hashCode(o);
			}

			@Override
			public boolean equals(Ingredient a, Ingredient b) {
				if (b == null) return false;
				var aValues = a.getValues();
				var bValues = b.getValues();
				if (aValues.length != bValues.length) return false;
				for (int i = 0; i < aValues.length; i++) {
					var aValue = aValues[i];
					var bValue = bValues[i];
					if (aValue.getClass() != bValue.getClass()) return false;
					if (aValue instanceof Ingredient.TagValue && bValue instanceof Ingredient.TagValue) {
						if (!aValue.equals(bValue)) {
							return false;
						}
					} else {
						if (aValue instanceof Ingredient.ItemValue(ItemStack item) && bValue instanceof Ingredient.ItemValue(ItemStack item1)) {
							if (MODERNFIX_DEDUPLICATION) {
								if (aValue != bValue) {
									return false;
								}
							} else {
								if (!ItemStack.isSameItemSameComponents(item, item1)){
									return false;
								}
							}
						} else return false;
					}
				}
				return true;
			}
		};
		INGREDIENT_CACHE = new ObjectOpenCustomHashSet<>(BASIC_HASH_STRATEGY);
		if (ModList.get().isLoaded("modernfix")) {
			MODERNFIX_DEDUPLICATION = ModernFixMixinPlugin.instance.isOptionEnabled("perf.ingredient_item_deduplication.IngredientMixin");
		} else MODERNFIX_DEDUPLICATION = false;
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
