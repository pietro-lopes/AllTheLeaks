package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.logisticscraft.occlusionculling.util.Vec3d;
import dev.tr7zw.entityculling.CullTask;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(value = CullTask.class, remap = false)
public class CullTaskMixin {

	@WrapOperation(method = "cullEntities", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z"))
	private boolean alt$shouldInterrupLoop(Iterator instance, Operation<Boolean> original, @Share(value = "entityIsNull") LocalBooleanRef boolRef){
		var origBool = original.call(instance);
		if (origBool && boolRef.get()) {
			return false;
		} else return origBool;
	}

	@Inject(method = "cullEntities", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/Iterator;next()Ljava/lang/Object;", shift = At.Shift.BY, by = 3))
	private void atl$grabEntity(Vec3 cameraMC, Vec3d camera, CallbackInfo ci, @Local Entity entity, @Share(value = "entityIsNull") LocalBooleanRef boolRef){
		if (entity == null) boolRef.set(true);
	}
}
