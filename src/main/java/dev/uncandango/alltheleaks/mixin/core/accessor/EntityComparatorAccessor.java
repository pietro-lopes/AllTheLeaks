package dev.uncandango.alltheleaks.mixin.core.accessor;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = {"journeymap.client.model.EntityHelper$EntityDistanceComparator", "journeymap.client.model.EntityHelper$EntityDTODistanceComparator"}, remap = false)
public interface EntityComparatorAccessor {
	@Mutable
	@Accessor(value = "player")
	void atl$setPlayer(Player player);

	@Accessor(value = "player")
	Player atl$getPlayer();
}