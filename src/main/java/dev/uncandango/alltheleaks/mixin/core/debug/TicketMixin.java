package dev.uncandango.alltheleaks.mixin.core.debug;

import dev.uncandango.alltheleaks.mixin.TicketExtension;
import net.minecraft.server.level.Ticket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Ticket.class)
public abstract class TicketMixin<T> implements TicketExtension<T> {
	@Shadow
	@Final
	private T key;

	@Override
	public T atl$getKey() {
		return this.key;
	}
}
