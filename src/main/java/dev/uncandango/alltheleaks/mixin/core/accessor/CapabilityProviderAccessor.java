package dev.uncandango.alltheleaks.mixin.core.accessor;

import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = CapabilityProvider.class, remap = false)
public interface CapabilityProviderAccessor {
	@Invoker("getCapabilities")
	CapabilityDispatcher getCapabilities();
}
