package dev.uncandango.alltheleaks.mixin.core.main;

import de.mari_023.ae2wtlib.AE2WTLibCreativeTab;
import de.mari_023.ae2wtlib.AE2wtlib;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = AE2WTLibCreativeTab.class, remap = false)
public class AE2WTLibCreativeTabMixin {
	@Shadow
	@Final
	private static List<ItemStack> items;

	@Inject(method = "buildDisplayItems", at = @At("HEAD"))
	private static void populateItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output, CallbackInfo ci){
		items.clear();
		AE2wtlibAccessor.atl$addToCreativeTab();
	}

	@Mixin(value = AE2wtlib.class,remap = false)
	public interface AE2wtlibAccessor {
		@Invoker("addToCreativeTab")
		static void atl$addToCreativeTab(){};
	}
}
