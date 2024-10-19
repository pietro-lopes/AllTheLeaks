package dev.uncandango.alltheleaks.mixin.core.accessor;

import me.desht.pneumaticcraft.client.gui.pneumatic_armor.ArmorMainScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Pseudo
@Mixin(value = ArmorMainScreen.class, remap = false)
public interface ArmorMainScreenAccessor {
    @Accessor("upgradeOptions")
    List<?> getUpgradeOptions();
}
