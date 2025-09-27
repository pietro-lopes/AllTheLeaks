package dev.uncandango.alltheleaks.mixin.core.main;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.mehvahdjukaar.moonlight.core.misc.FakeLevelManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;
import java.util.Map;

@Mixin(value = FakeLevelManager.class, remap = false)
public class FakeLevelManagerMixin {
	@CompatibleHashes(values = {-1241170928})
	@WrapOperation(method = "invalidateAll", at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;"))
	private static Collection<Object> toImmutableMap(Map instance, Operation<Collection<Object>> original){
		var map = original.call(instance);
		return Lists.newArrayList(map);
	}
}
