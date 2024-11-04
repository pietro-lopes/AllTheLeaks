package dev.uncandango.alltheleaks.mixin.core.main;

import de.maxhenkel.easyvillagers.blocks.tileentity.render.IronFarmRenderer;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = IronFarmRenderer.class, remap = false)
public class IronFarmRendererMixin implements UpdateableLevel<IronFarmRenderer> {

	@Shadow
	private ZombieRenderer zombieRenderer;

	@Shadow
	private Zombie zombie;

	@Shadow
	private IronGolem ironGolem;

	@Shadow
	private IronGolemRenderer ironGolemRenderer;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void registerInstance(BlockEntityRendererProvider.Context renderer, CallbackInfo ci){
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(ClientLevel level) {
		this.zombieRenderer = null;
		this.zombie = null;
		this.ironGolem = null;
		this.ironGolemRenderer = null;
	}
}
