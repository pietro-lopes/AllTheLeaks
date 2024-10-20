package dev.uncandango.alltheleaks.mixin.core.main;

import com.lowdragmc.lowdraglib.utils.DummyWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(value = DummyWorld.class, remap = false)
public class DummyWorldMixin {
	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;<init>(Lnet/minecraft/world/level/storage/WritableLevelData;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/core/Holder;Ljava/util/function/Supplier;ZZJI)V"), index = 4)
	private static Supplier<ProfilerFiller> replaceProfiler(Supplier<ProfilerFiller> profiler){
		return Minecraft.getInstance()::getProfiler;
	}
}
