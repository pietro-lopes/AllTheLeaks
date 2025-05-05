package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.desht.pneumaticcraft.api.pressure.PressureTier;
import me.desht.pneumaticcraft.common.block.entity.AbstractAirHandlingBlockEntity;
import me.desht.pneumaticcraft.common.block.entity.drone.ProgrammableControllerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ProgrammableControllerBlockEntity.class)
public class ProgrammableControllerBlockEntityMixin extends AbstractAirHandlingBlockEntity {
	public ProgrammableControllerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state, PressureTier pressureTier, int volume, int upgradeSlots) {
		super(type, pos, state, pressureTier, volume, upgradeSlots);
	}

	@WrapWithCondition(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/neoforged/bus/api/IEventBus;register(Ljava/lang/Object;)V"))
	private boolean registerOnServerOnly(IEventBus instance, Object o){
		return !Minecraft.getInstance().isSameThread();
	}
}
