package dev.uncandango.alltheleaks.mixin.core.accessor;

import net.minecraft.network.Connection;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FakePlayer.FakePlayerNetHandler.class)
public interface FakePlayerNetHandlerAccessor {
	@Accessor(value = "DUMMY_CONNECTION", remap = false)
	static Connection getDUMMY_CONNECTION(){
		return null;
	}
}
