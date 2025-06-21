package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryManager;
import net.neoforged.neoforge.registries.RegistrySnapshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@Mixin(RegistryManager.class)
public class RegistryManagerMixin {

	@WrapWithCondition(method = "lambda$applySnapshot$1", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/registries/RegistryManager;applySnapshot(Lnet/minecraft/core/MappedRegistry;Lnet/neoforged/neoforge/registries/RegistrySnapshot;Ljava/util/Set;)V"))
	private static boolean onlyIfNotLocal(MappedRegistry<Object> registry, RegistrySnapshot snapshot, Set<ResourceKey<?>> missingEntries) {
		return !Minecraft.getInstance().isSingleplayer();
	}
}
