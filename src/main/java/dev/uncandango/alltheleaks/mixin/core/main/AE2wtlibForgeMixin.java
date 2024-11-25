package dev.uncandango.alltheleaks.mixin.core.main;

import de.mari_023.ae2wtlib.AE2WTLibCreativeTab;
import de.mari_023.ae2wtlib.AE2wtlib;
import de.mari_023.ae2wtlib.AE2wtlibForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Consumer;

@Mixin(AE2wtlibForge.class)
public class AE2wtlibForgeMixin {
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/eventbus/api/IEventBus;addListener(Ljava/util/function/Consumer;)V", ordinal = 1))
	private <T> void atl$replaceConsumer(IEventBus instance, Consumer<T> tConsumer) {
		instance.addListener((BuildCreativeModeTabContentsEvent event) -> {
			if (event.getTabKey().location().equals(new ResourceLocation("ae2wtlib", "main"))) {
				try {
					var list = AE2WTLibCreativeTab.class.getDeclaredField("items");
					list.setAccessible(true);
					((List)list.get(null)).clear();
					var method = AE2wtlib.class.getDeclaredMethod("addToCreativeTab");
					method.invoke(null);
				} catch (NoSuchMethodException | NoSuchFieldException | InvocationTargetException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}
}
