package dev.uncandango.alltheleaks.mixin.core.main;

import dev.aaronhowser.mods.irregular_implements.block.block_entity.GlobalChatDetectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(GlobalChatDetectorBlockEntity.class)
public abstract class GlobalChatDetectorBlockEntityMixin extends BlockEntity {

	@Shadow
	@Final
	private static Set<?> globalDetectors;

	public GlobalChatDetectorBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public void onChunkUnloaded() {
		if (this.hasLevel() && !this.getLevel().isClientSide()) {
			globalDetectors.remove(this);
		}
	}
}
