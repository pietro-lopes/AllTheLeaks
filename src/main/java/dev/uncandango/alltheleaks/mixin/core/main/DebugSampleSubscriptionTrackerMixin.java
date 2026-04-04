package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.debugchart.DebugSampleSubscriptionTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(DebugSampleSubscriptionTracker.class)
public class DebugSampleSubscriptionTrackerMixin {
	@Inject(method = "handleSubscriptions", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;", shift = At.Shift.AFTER))
	private void removeFromIterator(long millis, int tick, CallbackInfo ci, @Local Iterator<?> iterator){
		iterator.remove();
	}
}
