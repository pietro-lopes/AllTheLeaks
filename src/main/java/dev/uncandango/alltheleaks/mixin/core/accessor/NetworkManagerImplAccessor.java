package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.google.common.collect.Multimap;
import dev.architectury.networking.forge.NetworkManagerImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = NetworkManagerImpl.class, remap = false)
public interface NetworkManagerImplAccessor {
    @Accessor("clientReceivables")
    static Multimap<Player, ResourceLocation> atl$getClientReveivables() {
        throw new AssertionError();
    }
}
