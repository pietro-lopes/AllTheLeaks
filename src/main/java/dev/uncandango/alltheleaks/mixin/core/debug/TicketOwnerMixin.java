package dev.uncandango.alltheleaks.mixin.core.debug;

import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.neoforged.neoforge.common.world.chunk.ForcedChunkManager$TicketOwner")
public class TicketOwnerMixin<T extends Comparable<? super T>> {
	@Shadow
	@Final
	private T owner;

	@Shadow
	@Final
	private ResourceLocation id;

	@Override
	public String toString() {
		return "TicketOwner[id=" + this.id + ", owner=" + this.owner + "]";
	}
}
