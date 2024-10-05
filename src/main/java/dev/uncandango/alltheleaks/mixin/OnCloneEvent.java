package dev.uncandango.alltheleaks.mixin;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public interface OnCloneEvent {
	void atl$onCloneEvent(PlayerEvent.Clone event);
}
