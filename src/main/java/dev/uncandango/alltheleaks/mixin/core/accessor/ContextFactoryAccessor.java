package dev.uncandango.alltheleaks.mixin.core.accessor;

import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.ContextFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ContextFactory.class, remap = false)
public interface ContextFactoryAccessor {
	@Accessor("currentContext")
	ThreadLocal<Context> getCurrentContext();
}
