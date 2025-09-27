//package dev.uncandango.alltheleaks.mixin.core.main;
//
//import com.llamalad7.mixinextras.sugar.Local;
//import net.minecraft.core.Holder;
//import net.minecraft.core.RegistryAccess;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.util.profiling.ProfilerFiller;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.TickingBlockEntity;
//import net.minecraft.world.level.dimension.DimensionType;
//import net.minecraft.world.level.storage.WritableLevelData;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.function.BooleanSupplier;
//import java.util.function.Supplier;
//
//@Mixin(ServerLevel.class)
//public abstract class ServerLevelMixin extends Level {
//	@Shadow
//	private int emptyTime;
//
//	protected ServerLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
//		super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
//	}
//
//	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V", ordinal = 4))
//	private void clearRemovedTickers(BooleanSupplier hasTimeLeft, CallbackInfo ci, @Local boolean flag){
//		if (!flag && this.emptyTime > 300 && !this.blockEntityTickers.isEmpty()) {
//			this.blockEntityTickers.removeIf(TickingBlockEntity::isRemoved);
//		}
//	}
//}
