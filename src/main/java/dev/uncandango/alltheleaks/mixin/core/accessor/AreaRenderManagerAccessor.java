package dev.uncandango.alltheleaks.mixin.core.accessor;

import me.desht.pneumaticcraft.client.render.area.AreaRenderManager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = AreaRenderManager.class, remap = false)
public interface AreaRenderManagerAccessor {
    @Accessor("level")
    void atl$setLevel(Level level);
}
