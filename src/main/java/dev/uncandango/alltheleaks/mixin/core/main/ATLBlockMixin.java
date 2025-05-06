package dev.uncandango.alltheleaks.mixin.core.main;

import de.dafuqs.additionalentityattributes.Support;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;

@Mixin(Block.class)
public class ATLBlockMixin {

		@Unique
		private WeakReference<Player> additionalEntityAttributes$breakingPlayer = new WeakReference<>(null);

		@Inject(
			method = {"playerDestroy"},
			at = {@At("HEAD")}
		)
		public void additionalEntityAttributes$saveBreakingPlayer(
			Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack, CallbackInfo callbackInfo
		) {
			this.additionalEntityAttributes$breakingPlayer = new WeakReference<>(player);
		}

		@ModifyArg(
			method = {"popExperience"},
			at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"
			)
		)
		private int additionalEntityAttributes$modifyExperience(int originalXP) {
			return (int)(originalXP * Support.getExperienceMod(this.additionalEntityAttributes$breakingPlayer.get()));
		}


}
