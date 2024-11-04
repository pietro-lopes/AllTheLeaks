package dev.uncandango.alltheleaks.mixin.core.accessor;

import net.minecraft.network.Connection;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = FakePlayer.FakePlayerNetHandler.class, remap = false)
public interface FakePlayerAccessor {

    @Accessor("DUMMY_CONNECTION")
    static @Nullable Connection getDUMMY_CONNECTION() {
		throw new AssertionError();
	}

}
