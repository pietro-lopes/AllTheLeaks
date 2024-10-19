package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripItem;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = ExtendoGripItem.class, remap = false)
public interface ExtendoGripItemAccessor {
    @Mutable
    @Accessor("lastActiveDamageSource")
    static void setLastActiveDamageSource(DamageSource damage) {
    }

}
