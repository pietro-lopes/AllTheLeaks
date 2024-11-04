package dev.uncandango.alltheleaks.mixin.core.accessor;

import appeng.client.guidebook.hotkey.OpenGuideHotkey;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = OpenGuideHotkey.class, remap = false)
public interface OpenGuideHotkeyAccessor {
    @Accessor("lastStack")
    static void atl$setLastStack(ItemStack stack){}
}
