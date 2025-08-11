package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.Trackable;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements Trackable {
	@Override
	public Class<?> atl$getBaseClass() {
		return MinecraftServer.class;
	}
}
