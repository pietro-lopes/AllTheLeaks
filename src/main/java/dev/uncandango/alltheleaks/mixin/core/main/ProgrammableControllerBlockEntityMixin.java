package dev.uncandango.alltheleaks.mixin.core.main;

import me.desht.pneumaticcraft.api.pressure.PressureTier;
import me.desht.pneumaticcraft.common.block.entity.AbstractAirHandlingBlockEntity;
import me.desht.pneumaticcraft.common.block.entity.ProgrammableControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ProgrammableControllerBlockEntity.class, remap = false)
public abstract class ProgrammableControllerBlockEntityMixin extends AbstractAirHandlingBlockEntity {
	public ProgrammableControllerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state, PressureTier pressureTier, int volume, int upgradeSlots) {
		super(type, pos, state, pressureTier, volume, upgradeSlots);
	}

	@Override
	public void setLevel(Level level) {
		super.setLevel(level);
		if (level != null && level.isClientSide()) {
			MinecraftForge.EVENT_BUS.unregister(this);
		}
	}
}
