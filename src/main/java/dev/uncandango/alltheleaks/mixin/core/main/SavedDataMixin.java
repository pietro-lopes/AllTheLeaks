package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.nio.file.Path;

@Mixin(SavedData.class)
public class SavedDataMixin {
	@Shadow
	@Final
	private static Logger LOGGER;

	@WrapOperation(method = "lambda$save$0", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/IOUtilities;writeNbtCompressed(Lnet/minecraft/nbt/CompoundTag;Ljava/nio/file/Path;)V"))
	private void logFileIfError(CompoundTag tag, Path path, Operation<Void> original){
		try {
			original.call(tag, path);
		} catch (Throwable e) {
			LOGGER.error("File " + path.getFileName() + " not saved...", e);
			throw e;
		}
	}
}
