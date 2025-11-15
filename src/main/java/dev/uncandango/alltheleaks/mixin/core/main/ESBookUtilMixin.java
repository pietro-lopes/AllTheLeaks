package dev.uncandango.alltheleaks.mixin.core.main;

import cn.leolezury.eternalstarlight.common.util.ESBookUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ESBookUtil.class)
public class ESBookUtilMixin {
	@Inject(method = "unlock", at = @At("HEAD"), cancellable = true)
	private static void alwaysCancelUnlock(ServerPlayer player, ResourceLocation[] locations, CallbackInfo ci){
		ci.cancel();
	}
}
