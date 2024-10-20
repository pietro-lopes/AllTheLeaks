package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.AllTheLeaks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientFilter;
import mezz.jei.api.runtime.IIngredientListOverlay;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.api.runtime.IRecipesGui;
import mezz.jei.library.load.PluginCaller;
import mezz.jei.library.startup.JeiStarter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Pseudo
@Mixin(value = JeiStarter.class, remap = false)
public class JeiStarterMixin {
    @Shadow
    @Final
    private List<IModPlugin> plugins;

    @Inject(method = "stop()V", at = @At(value = "TAIL"))
    private void atl$clearRuntime(CallbackInfo ci) {
        Class<?>[] candidates = new Class[]{IJeiRuntime.class, IJeiHelpers.class, IRecipeManager.class, IRecipesGui.class, IIngredientListOverlay.class, IIngredientFilter.class, IRecipeCategory.class};
        PluginCaller.callOnPlugins("Clearing jeiRuntime from plugins...", this.plugins, p -> {
            Arrays.stream(p.getClass().getDeclaredFields()).filter(f -> Arrays.stream(candidates).anyMatch(clazz -> clazz.isAssignableFrom(f.getType()))).forEach(f -> {
                f.setAccessible(true);
                try {
                    if (f.getModifiers() >= 8) {
                        f.set(null, null);
                    } else {
                        f.set(p, null);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                AllTheLeaks.LOGGER.info("Cleared {} from plugin: {}", f.getType().getSimpleName(), p.getClass().getName());
            });
        });
    }
}
