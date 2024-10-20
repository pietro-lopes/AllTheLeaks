package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.cominixo.betterf3.modules.LocationModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;
import java.util.concurrent.CompletableFuture;

@Mixin(value = LocationModule.class, remap = false)
public class LocationModuleMixin {
	@Unique
	private WeakReference<CompletableFuture<LevelChunk>> atl$weakChunkFuture = new WeakReference<>(null);

	@Inject(method = "update", at = @At(value = "FIELD", target = "Lme/cominixo/betterf3/modules/LocationModule;chunkFuture:Ljava/util/concurrent/CompletableFuture;", ordinal = 0))
	private void injectLocalVar(Minecraft client, CallbackInfo ci, @Share("localChunkFuture") LocalRef<CompletableFuture<LevelChunk>> localChunkFuture) {
		localChunkFuture.set(atl$weakChunkFuture.get());
	}

	@WrapOperation(method = "update", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/concurrent/CompletableFuture;thenApply(Ljava/util/function/Function;)Ljava/util/concurrent/CompletableFuture;"))
	private void assignValue(LocationModule instance, @Nullable CompletableFuture<LevelChunk> value, Operation<Void> original, @Share("localChunkFuture") LocalRef<CompletableFuture<LevelChunk>> localChunkFuture) {
		atl$weakChunkFuture = new WeakReference<>(value);
		localChunkFuture.set(value);
	}

	@WrapOperation(method = "update", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/concurrent/CompletableFuture;completedFuture(Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;"))
	private void assignValueClient(LocationModule instance, @Nullable CompletableFuture<LevelChunk> value, Operation<Void> original, @Share("localChunkFuture") LocalRef<CompletableFuture<LevelChunk>> localChunkFuture) {
		atl$weakChunkFuture = new WeakReference<>(value);
		localChunkFuture.set(value);
	}

	@WrapOperation(method = "update", at = @At(value = "FIELD", target = "Lme/cominixo/betterf3/modules/LocationModule;chunkFuture:Ljava/util/concurrent/CompletableFuture;", ordinal = 0))
	private @Nullable CompletableFuture<LevelChunk> swapValue0(LocationModule instance, Operation<CompletableFuture<LevelChunk>> original, @Share("localChunkFuture") LocalRef<CompletableFuture<LevelChunk>> localChunkFuture) {
		return localChunkFuture.get();
	}

	@WrapOperation(method = "update", at = @At(value = "FIELD", target = "Lme/cominixo/betterf3/modules/LocationModule;chunkFuture:Ljava/util/concurrent/CompletableFuture;", ordinal = 2))
	private @Nullable CompletableFuture<LevelChunk> swapValue2(LocationModule instance, Operation<CompletableFuture<LevelChunk>> original, @Share("localChunkFuture") LocalRef<CompletableFuture<LevelChunk>> localChunkFuture) {
		return localChunkFuture.get();
	}

	@WrapOperation(method = "update", at = @At(value = "FIELD", target = "Lme/cominixo/betterf3/modules/LocationModule;chunkFuture:Ljava/util/concurrent/CompletableFuture;", ordinal = 4))
	private @Nullable CompletableFuture<LevelChunk> swapValue4(LocationModule instance, Operation<CompletableFuture<LevelChunk>> original, @Share("localChunkFuture") LocalRef<CompletableFuture<LevelChunk>> localChunkFuture) {
		return localChunkFuture.get();
	}
}
