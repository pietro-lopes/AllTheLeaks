package dev.uncandango.alltheleaks.mixin.core.main;

import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.uncandango.alltheleaks.mixin.EventKey;
import me.desht.pneumaticcraft.common.entity.drone.DroneEntity;
import net.neoforged.bus.api.IEventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.function.Consumer;

@Mixin(value = DroneEntity.class)
public class DroneEntityMixin implements EventKey {
	@Unique
	private final Map<String, Consumer<?>> keyMap = Maps.newHashMap();

	@WrapOperation(method = "onFirstTick", at = @At(value = "INVOKE", target = "Lnet/neoforged/bus/api/IEventBus;addListener(Ljava/util/function/Consumer;)V"))
	private void atl$registerEntityObjectInsteadOfConsumer(IEventBus instance, Consumer<?> tConsumer, Operation<Void> original) {
		original.call(instance, tConsumer);
		keyMap.computeIfAbsent("onSemiblockEvent", key -> tConsumer);
	}

	@Override
	public Map<String, Consumer<?>> atl$getKeyMap() {
		return keyMap;
	}
}
