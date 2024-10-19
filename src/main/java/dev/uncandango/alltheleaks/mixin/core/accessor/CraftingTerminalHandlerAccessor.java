package dev.uncandango.alltheleaks.mixin.core.accessor;

import de.mari_023.ae2wtlib.wct.CraftingTerminalHandler;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.WeakHashMap;

@Pseudo
@Mixin(value = CraftingTerminalHandler.class, remap = false)
public interface CraftingTerminalHandlerAccessor {
    @Accessor("players")
    static WeakHashMap<Player, CraftingTerminalHandler> atl$getPlayers() {
        return new WeakHashMap<>();
    }

}
