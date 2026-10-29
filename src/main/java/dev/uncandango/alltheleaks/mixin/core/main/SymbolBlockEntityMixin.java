package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.hyperlynx.reactive.be.CrucibleBlockEntity;
import dev.hyperlynx.reactive.be.SymbolBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(SymbolBlockEntity.class)
public class SymbolBlockEntityMixin extends BlockEntity {
	public SymbolBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	@WrapWithCondition(method = "<init>(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/Item;)V", at = @At(value = "INVOKE", target = "Lnet/neoforged/bus/api/IEventBus;register(Ljava/lang/Object;)V"))
	private boolean alwaysCancel(IEventBus instance, Object o){
		return false;
	}

	@Override
	public void onLoad() {
		if (Objects.nonNull(this.level) && !this.level.isClientSide()) {
			NeoForge.EVENT_BUS.register(this);
		}
	}

	@Override
	public void setRemoved() {
		NeoForge.EVENT_BUS.unregister(this);
		super.setRemoved();
	}
}
