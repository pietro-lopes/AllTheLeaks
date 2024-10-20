package dev.uncandango.alltheleaks.mods;

import net.minecraft.client.renderer.entity.EntityRenderer;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

import static dev.uncandango.alltheleaks.mods.MinecraftClient.clearEntityFromFields;

// TODO: Refactor!
public interface Geckolib {
    static void clearRenderer(EntityRenderer<?> renderer) {
        if (renderer instanceof GeoReplacedEntityRenderer<?, ?> replacedEntityRenderer) {
            clearEntityFromFields(replacedEntityRenderer);
        }
    }

//    static void run() {
//        Minecraft.getInstance().getEntityRenderDispatcher().renderers.values().forEach(renderer -> {
//            if (!ReflectionHelper.setFieldWithCheck(renderer, GeoEntityRenderer.class, "animatable", null))
//                ReflectionHelper.setFieldWithCheck(renderer, GeoReplacedEntityRenderer.class, "animatable", null);
//            /*
//            if (renderer instanceof GeoEntityRenderer<?> geoRenderer){
//                var animatable = geoRenderer.getAnimatable();
//                if (animatable != null) {
//                    ReflectionHelper.setFieldWithCheck(geoRenderer, GeoEntityRenderer.class, "animatable", null);
//                    AllTheLeaks.LOGGER.info("Cleared entity from {}", geoRenderer.getClass());
//                    return;
//                }
//            }
//            if (renderer instanceof GeoReplacedEntityRenderer<?,?> geoReplaceRenderer){
//                var animatable = geoReplaceRenderer.getAnimatable();
//                if (animatable != null) {
//
//                    AllTheLeaks.LOGGER.info("Cleared entity from {}", geoReplaceRenderer.getClass());
//                }
//            }
//
//             */
//        });

    // Clear entity from renderer that any mod that puts in it

//        Minecraft.getInstance().getEntityRenderDispatcher().renderers.values().forEach(renderer -> {
//            if (renderer instanceof LivingEntityRenderer<?, ?> livingRenderer) {
//                var model = livingRenderer.getModel();
//                clearEntityFromFields(model);
//            }
//
//        });
//        ForgeRegistries.ITEMS.getValues().forEach(item -> {
//            var properties = item.getRenderPropertiesInternal();
//            if (properties == null) return;
//            Arrays.stream(properties.getClass().getDeclaredFields())
//                    .filter(field -> GeoArmorRenderer.class.isAssignableFrom(field.getType()))
//                    .forEach(field -> {
//                        Object renderer = null;
//                        try {
//                            field.setAccessible(true);
//                            renderer = field.get(properties);
//                        } catch (Exception e) {
//                            AllTheLeaks.LOGGER.warn("Was not possible to get renderer from {}", properties.getClass());
//                        }
//                        clearEntityFromFields(renderer);
//                    });
//        });
//    }

//    static <T> void replaceEntityWithNull(Class<T> clazz, T parent) {
//        if (parent instanceof GeoEntityRenderer<?> geoRenderer) {
//            var animatable = geoRenderer.getAnimatable();
//            if (animatable != null) {
//                ReflectionHelper.setField(parent, clazz, "animatable", null);
//            }
//        }
//    }

}
