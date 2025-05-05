//package dev.uncandango.alltheleaks.mixin.core.main;
//
//import dev.uncandango.alltheleaks.AllTheLeaks;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.client.particle.BreakingItemParticle;
//import net.minecraft.client.particle.TextureSheetParticle;
//import net.minecraft.world.item.ItemStack;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(BreakingItemParticle.class)
//public abstract class BreakingItemParticleMixin extends TextureSheetParticle {
//
//	protected BreakingItemParticleMixin(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
//		super(level, x, y, z, xSpeed, ySpeed, zSpeed);
//	}
//
//	@Inject(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDLnet/minecraft/world/item/ItemStack;)V", at = @At("RETURN"))
//	private void debugSprite(ClientLevel level, double x, double y, double z, ItemStack stack, CallbackInfo ci) {
//		AllTheLeaks.LOGGER.info("BreakingItemParticle {} at {} {} {} with sprite {}", stack, x, y, z, this.sprite);
//	}
//}
