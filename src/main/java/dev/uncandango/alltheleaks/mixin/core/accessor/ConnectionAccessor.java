package dev.uncandango.alltheleaks.mixin.core.accessor;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Connection.class)
public interface ConnectionAccessor {
	@Accessor("packetListener")
	void setPacketListener(PacketListener packet);
}
