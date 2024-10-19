package dev.uncandango.alltheleaks.mixin.core.main;

import com.aetherteam.aether.capability.item.DroppedItemCapability;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(value = DroppedItemCapability.class, remap = false)
public class DroppedItemCapabilityMixin {
	@Shadow
	@Final
	private ItemEntity itemEntity;
	@Unique
	private UUID atl$ownerUUID;

	/**
	 * @author Uncandango
	 * @reason This is the proper way to get the owner
	 */
	@Overwrite
	public Entity getOwner() {
		if (atl$ownerUUID == null) {
			return null;
		}
		if (this.itemEntity.level() instanceof ServerLevel serverLevel) {
			return serverLevel.getEntity(atl$ownerUUID);
		}
		return null;
	}

	/**
	 * @author Uncandango
	 * @reason This is the proper way to set the owner
	 */
	@Overwrite
	public void setOwner(Entity owner) {
		if (owner != null){
			atl$ownerUUID = owner.getUUID();
		}
	}
}
