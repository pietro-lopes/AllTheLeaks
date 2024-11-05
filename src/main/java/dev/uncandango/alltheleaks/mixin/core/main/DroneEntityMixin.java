package dev.uncandango.alltheleaks.mixin.core.main;

import me.desht.pneumaticcraft.common.entity.drone.AbstractDroneEntity;
import me.desht.pneumaticcraft.common.entity.drone.DroneEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = DroneEntity.class, remap = false)
public abstract class DroneEntityMixin extends AbstractDroneEntity {

	public DroneEntityMixin(EntityType<? extends PathfinderMob> type, Level world) {
		super(type, world);
	}

	@Override
	public void onRemovedFromWorld() {
		super.onRemovedFromWorld();
		if (!this.level().isClientSide()) {
			MinecraftForge.EVENT_BUS.unregister(this);
		}
	}
}
