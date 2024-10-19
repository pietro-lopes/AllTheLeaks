package dev.uncandango.alltheleaks.mods;

import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.data.pack.GTDynamicDataPack;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import dev.uncandango.alltheleaks.mixin.ModularUIExtension;
import dev.uncandango.alltheleaks.mixin.core.accessor.PatternPreviewWidgetAccessor;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public interface Gtceu {
    List<WeakReference<ModularUI>> modularUis = new ArrayList<>();

    static void run(MinecraftServer server) {
        GTDynamicDataPack.clearServer();
        if (server instanceof IntegratedServer) {
            PatternPreviewWidgetAccessor.atl$setLevel(null);
            PatternPreviewWidgetAccessor.atl$getCache().clear();
        }
        for (RecipeType<?> recipeType : ForgeRegistries.RECIPE_TYPES) {
            if (recipeType instanceof GTRecipeType gt) {
                //noinspection UnstableApiUsage
                gt.getLookup().removeAllRecipes();
            }
        }
        updateModularUis(null);
    }

    static void setDummyWorld(LevelAccessor level) {
        PatternPreviewWidgetAccessor.atl$setLevel(new TrackedDummyWorld((Level) level));
        PatternPreviewWidgetAccessor.atl$getCache().clear();
//        PatternPreviewWidgetAccessor.atl$getCache().values().forEach(patterns -> {
//            for (var pattern : patterns){
//                try {
//                    var clazz = Class.forName("com.gregtechceu.gtceu.api.gui.widget.PatternPreviewWidget$MBPattern");
//                    var field = clazz.getDeclaredField("controllerBase");
//                    field.setAccessible(true);
//                    var controller = (IMultiController)field.get(pattern);
//                    var levelBe = controller.self().holder.self().getLevel();
//                    if (levelBe instanceof TrackedDummyWorld) {
//                        controller.self().holder.self().setLevel(PatternPreviewWidgetAccessor.atl$getLevel());
//                    }
////                    controller.self(). new MultiblockState(PatternPreviewWidgetAccessor.atl$getLevel(), controller.self().getPos())
//
//                } catch (Exception e){
//                    AllTheLeaks.LOGGER.error("Error while cleaning GTCEU Patterns", e);
//                }
//            }
//        });
    }

    static void updateModularUis(@Nullable Player player){
        for (var it = modularUis.iterator(); it.hasNext();){
            var modularUi = it.next().get();
            if (modularUi == null) {
                it.remove();
                continue;
            }
            //noinspection ConstantValue
            if (modularUi.entityPlayer instanceof AbstractClientPlayer && (Object)modularUi instanceof ModularUIExtension extension){
                extension.atl$setEntityPlayer(player);
            }
        }
        ((ArrayList<?>) modularUis).trimToSize();
    }

}
