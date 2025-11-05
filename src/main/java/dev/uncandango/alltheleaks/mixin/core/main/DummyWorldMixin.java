package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.sugar.Local;
import com.lowdragmc.lowdraglib.utils.DummyWorld;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(value = DummyWorld.class, remap = false)
public abstract class DummyWorldMixin extends Level {
	protected DummyWorldMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
		super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
	}

	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;<init>(Lnet/minecraft/world/level/storage/WritableLevelData;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/core/Holder;Ljava/util/function/Supplier;ZZJI)V"), index = 4)
	private static Supplier<ProfilerFiller> replaceProfiler(Supplier<ProfilerFiller> profiler, @Local(argsOnly = true) Level level){
		return level.getProfilerSupplier();
	}
}
