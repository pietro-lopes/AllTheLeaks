package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.uncandango.alltheleaks.leaks.client.mods.journeymap.UntrackedIssue002;
import journeymap.client.render.draw.MobIconCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;

// NOT BEING USED, NEVER TRIGGERED
@Mixin(MobIconCache.class)
public class MobIconCacheMixin {
	@WrapOperation(method = {"getMobIcon"}, at = @At(value = "INVOKE", target = "Ljava/util/HashMap;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
	private static Object atl$reloaseOldIconCache(HashMap instance, Object key, Object value, Operation<Object> original) {
		var iconCache = original.call(instance, key, value);
		if (iconCache != null) {
			try {
				UntrackedIssue002.REMOVE.invoke(iconCache);
			} catch (Throwable ignored) {}
		}
		return iconCache;
	}
}
