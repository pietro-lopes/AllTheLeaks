package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.core.accessor.EntityComparatorAccessor;
import journeymap.client.model.EntityDTO;
import journeymap.client.model.EntityHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static journeymap.client.model.EntityHelper.entityDTODistanceComparator;
import static journeymap.client.model.EntityHelper.entityDistanceComparator;

@Mixin(value = EntityHelper.class, remap = false)
public class EntityHelperMixin {
	@Inject(method = "getEntitiesNearby", at = @At(value = "INVOKE", target = "Ljava/util/Collections;sort(Ljava/util/List;Ljava/util/Comparator;)V", shift = At.Shift.AFTER))
	private static void clearPlayer(String timerName, int maxEntities, boolean hostile, Class<?>[] entityClasses, CallbackInfoReturnable<List<EntityDTO>> cir) {
		if (entityDTODistanceComparator instanceof EntityComparatorAccessor accessor) {
			accessor.setPlayer(null);
		}
	}

	@Inject(method = "getPlayersNearby", at = @At(value = "INVOKE", target = "Ljava/util/Collections;sort(Ljava/util/List;Ljava/util/Comparator;)V", shift = At.Shift.AFTER))
	private static void clearPlayer(CallbackInfoReturnable<List<EntityDTO>> cir) {
		if (entityDistanceComparator instanceof EntityComparatorAccessor accessor) {
			accessor.setPlayer(null);
		}
	}
}
