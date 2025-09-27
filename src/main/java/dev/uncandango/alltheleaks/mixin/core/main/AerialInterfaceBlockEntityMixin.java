package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import me.desht.pneumaticcraft.api.pressure.PressureTier;
import me.desht.pneumaticcraft.common.block.entity.AbstractAirHandlingBlockEntity;
import me.desht.pneumaticcraft.common.block.entity.AerialInterfaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AerialInterfaceBlockEntity.class)
public abstract class AerialInterfaceBlockEntityMixin extends AbstractAirHandlingBlockEntity {
	public AerialInterfaceBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state, PressureTier pressureTier, int volume, int upgradeSlots) {
		super(type, pos, state, pressureTier, volume, upgradeSlots);
	}

	@CompatibleHashes(values = {2144584950})
	@WrapWithCondition(method = "clearRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/eventbus/api/IEventBus;register(Ljava/lang/Object;)V", remap = false))
	private boolean registerIfServerSide(IEventBus instance, Object o){
		if (o instanceof AerialInterfaceBlockEntity aibe) {
			return aibe.hasLevel() && !aibe.getLevel().isClientSide();
		}
		return false;
	}
}
