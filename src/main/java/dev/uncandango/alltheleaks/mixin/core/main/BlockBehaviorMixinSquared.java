package dev.uncandango.alltheleaks.mixin.core.main;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockBehaviour.class, priority = 1100)
public abstract class BlockBehaviorMixinSquared {
	@Shadow
	Explosion malum$explosion;

	@TargetHandler(mixin = "com.sammy.malum.mixin.BlockBehaviorMixin", name = "malum$getBlockDrops")
	@Inject(method = "@MixinSquared:Handler", at = @At("TAIL"))
	private void clearMalumExplosion(LootParams.Builder builder, CallbackInfoReturnable<LootParams.Builder> cir){
		malum$explosion = null;
	}
}
