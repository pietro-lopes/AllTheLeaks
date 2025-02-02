package dev.uncandango.alltheleaks.mixin.core.main;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "dev.latvian.mods.kubejs.recipe.CachedTagLookup$1")
public class CachedTagLookupMixin<T> {
	@Redirect(method = "element", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;get(Lnet/minecraft/resources/ResourceLocation;)Ljava/lang/Object;"))
	private Object redirectElement(Registry<T> instance, ResourceLocation resourceLocation) {
		return instance.getOptional(resourceLocation).orElse(null);
	}
}
