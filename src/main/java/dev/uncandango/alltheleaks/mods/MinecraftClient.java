package dev.uncandango.alltheleaks.mods;

import dev.uncandango.alltheleaks.mixin.core.accessor.LevelRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.searchtree.RefreshableSearchTree;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.world.entity.Entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static dev.uncandango.alltheleaks.AllTheLeaks.LOGGER;

public interface MinecraftClient {
    static void run() {
        LOGGER.debug("Cleaning Minecraft...");
        Minecraft.getInstance().crosshairPickEntity = null;
        Minecraft.getInstance().hitResult = null;
        Minecraft.getInstance().getEntityRenderDispatcher().crosshairPickEntity = null;
        Minecraft.getInstance().getBlockEntityRenderDispatcher().cameraHitResult = null;

        if (Minecraft.getInstance().levelRenderer instanceof LevelRendererAccessor accessor) {
            accessor.atl$getRecentlyCompiledChunks().clear();
        }
        Minecraft.getInstance().getEntityRenderDispatcher().renderers.values().forEach(renderer -> {
            clearEntityFromFields(renderer);
            if (renderer instanceof LivingEntityRenderer<?, ?> livingRenderer) {
                var model = livingRenderer.getModel();
                clearEntityFromFields(model);
            }
        });
        var connection = Minecraft.getInstance().getConnection();
        clearEntityFromFields(connection);

        Minecraft.getInstance().getSearchTreeManager().register(SearchRegistry.RECIPE_COLLECTIONS, (list) -> RefreshableSearchTree.empty());
    }

    static void clearEntityFromFields(Object obj) {
        if (obj == null) return;
        grabAllFields(obj).stream()
                .filter(field -> Entity.class.isAssignableFrom(field.getType()))
                .forEach(field -> {
                    var wasNull = true;
                    try {
                        field.setAccessible(true);
                        if (field.get(obj) != null) wasNull = false;
                        field.set(obj, null);
                    } catch (Exception e) {
                        LOGGER.warn("Was not possible to clear entity from {}", obj.getClass());
                    }
                    if (!wasNull) LOGGER.info("Cleared entity from {}", obj.getClass());
                });
    }

    static List<Field> grabAllFields(Object obj) {
        var list = new ArrayList<Field>();
        var currentClass = obj.getClass();
        while (currentClass != Object.class) {
            list.addAll(List.of(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }
        return list;
    }
}
