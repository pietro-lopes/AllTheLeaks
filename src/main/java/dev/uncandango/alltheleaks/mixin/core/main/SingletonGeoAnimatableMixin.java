package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;

@Mixin(SingletonGeoAnimatable.class)
public interface SingletonGeoAnimatableMixin {
	@WrapMethod(method = "registerSyncedAnimatable")
	private static void cancelIfBlockEntity(GeoAnimatable animatable, Operation<Void> original){
		if (!(animatable instanceof BlockEntity)) original.call(animatable);
	}
}
