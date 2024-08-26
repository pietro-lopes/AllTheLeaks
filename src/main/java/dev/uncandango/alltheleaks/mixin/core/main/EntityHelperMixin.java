package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.leaks.client.mods.journeymap.UntrackedIssue001;
import journeymap.client.model.EntityDTO;
import journeymap.client.model.EntityHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EntityHelper.class)
public abstract class EntityHelperMixin {

	@Inject(method = "getPlayersNearby", at = @At(value = "INVOKE", target = "Ljava/util/Collections;sort(Ljava/util/List;Ljava/util/Comparator;)V", shift = At.Shift.AFTER))
	private static void atl$clearPlayerFromComparator(CallbackInfoReturnable<List<EntityDTO>> cir) {
		UntrackedIssue001.clearPlayerFromComparator();
	}

	@Inject(method = "getEntitiesNearby", at = @At(value = "INVOKE", target = "Ljava/util/Collections;sort(Ljava/util/List;Ljava/util/Comparator;)V", shift = At.Shift.AFTER))
	private static void atl$clearPlayerFromDTOComparator(CallbackInfoReturnable<List<EntityDTO>> cir) {
		UntrackedIssue001.clearPlayerFromDTOComparator();
	}
}
