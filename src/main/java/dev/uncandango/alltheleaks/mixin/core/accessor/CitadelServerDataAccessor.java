package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.github.alexthe666.citadel.server.world.CitadelServerData;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashMap;
import java.util.Map;

@Pseudo
@Mixin(value = CitadelServerData.class, remap = false)
public interface CitadelServerDataAccessor {
    @Accessor("dataMap")
    static Map<MinecraftServer, CitadelServerData> atl$getDataMap() {
        return new HashMap<>();
    }

}
