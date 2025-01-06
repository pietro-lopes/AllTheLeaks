package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.uncandango.alltheleaks.AllTheLeaks;
import journeymap.client.model.ImageHolder;
import journeymap.client.model.ImageSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(ImageSet.class)
public class ImageSetMixin {
	@WrapOperation(method = "addHolder(Ljourneymap/client/model/ImageHolder;)Ljourneymap/client/model/ImageHolder;", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
	private Object atl$releaseOldImageHolder(Map instance, Object k, Object v, Operation<ImageHolder> original) {
		var replacement = original.call(instance, k, v);
		if (replacement != null) {
			AllTheLeaks.LOGGER.info("Clearing ImageHolder that was replaced...");
			replacement.clear();
		}
		return replacement;
	}
}
