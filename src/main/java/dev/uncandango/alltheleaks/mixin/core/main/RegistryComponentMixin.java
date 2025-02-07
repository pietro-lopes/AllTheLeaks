package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.component.RegistryComponent;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RegistryComponent.class)
public class RegistryComponentMixin<T> {
	@ModifyArg(method = "lambda$static$0", at = @At(value = "INVOKE", target = "Ldev/latvian/mods/kubejs/recipe/component/RegistryComponent;<init>(Lnet/minecraft/core/Registry;Ldev/latvian/mods/kubejs/registry/RegistryType;Lcom/mojang/serialization/Codec;)V"), index = 2)
	private static <T> Codec<T> modifyArg(Codec<T> codec, @Local ResourceKey<Registry<T>> key, @Local(argsOnly = true) RegistryAccessContainer access) {
		return access.access().registry(key).orElseThrow().byNameCodec();
	}
}
