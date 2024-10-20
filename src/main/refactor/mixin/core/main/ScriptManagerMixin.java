package dev.uncandango.alltheleaks.mixin.core.main;


import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.Context;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Pseudo
@Mixin(value = ScriptManager.class, remap = false)
public class ScriptManagerMixin {
    @Shadow
    @Final
    private static ThreadLocal<Context> CURRENT_CONTEXT;
    @Shadow
    public Context context;
    @Shadow
    @Final
    public ScriptType scriptType;

    @Inject(method = "unload", at = @At("RETURN"))
    private void atl$clearContext(CallbackInfo ci) {
        if (this.context == null && this.scriptType.isServer()) {
            CompletableFuture.runAsync(CURRENT_CONTEXT::remove, Minecraft.getInstance());
        }
    }

    public static class NetworkManagerImplMixin {
    }
}
