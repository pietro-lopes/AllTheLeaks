package dev.uncandango.alltheleaks.mixin.accessor;

import mods.railcraft.charge.ChargeNetworkImpl;
import mods.railcraft.charge.ChargeProviderImpl;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = ChargeProviderImpl.class, remap = false)
public interface ChargeProviderImplAccessor {
	@Accessor("networks")
	Map<ServerLevel, ChargeNetworkImpl> getNetworks();
}
