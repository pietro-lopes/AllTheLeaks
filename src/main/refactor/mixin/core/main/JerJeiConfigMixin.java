package dev.uncandango.alltheleaks.mixin.core.main;

import jeresources.jei.JEIConfig;
import jeresources.registry.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.runtime.IJeiRuntime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

@Pseudo
@Mixin(value = JEIConfig.class, remap = false)
public abstract class JerJeiConfigMixin implements IModPlugin {
    @Shadow
    private static IJeiRuntime jeiRuntime;

    @Shadow
    private static IJeiHelpers jeiHelpers;

    @Override
    public void onRuntimeUnavailable() {
        jeiRuntime = null;
        jeiHelpers = null;
        DungeonRegistry.getInstance().clear();
        MobRegistry.getInstance().clear();
        PlantRegistry.getInstance().clear();
        VillagerRegistry.getInstance().clear();
        WorldGenRegistry.getInstance().clear();
    }
}
