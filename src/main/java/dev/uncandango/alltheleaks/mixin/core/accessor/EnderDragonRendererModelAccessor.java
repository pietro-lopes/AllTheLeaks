package dev.uncandango.alltheleaks.mixin.core.accessor;

import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnderDragonRenderer.DragonModel.class)
public interface EnderDragonRendererModelAccessor {
    @Mutable
    @Accessor("entity")
    void atl$setEntity(EnderDragon dragon);

}
