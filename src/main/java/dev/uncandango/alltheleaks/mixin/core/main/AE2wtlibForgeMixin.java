package dev.uncandango.alltheleaks.mixin.core.main;

import de.mari_023.ae2wtlib.AE2wtlibForge;
import net.minecraftforge.eventbus.api.IEventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

@Mixin(value = AE2wtlibForge.class, remap = false)
public class AE2wtlibForgeMixin {
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/eventbus/api/IEventBus;addListener(Ljava/util/function/Consumer;)V", ordinal = 1))
	private <T> void atl$dontRegister(IEventBus instance, Consumer<T> tConsumer) {
		// do nothing
	}
}
