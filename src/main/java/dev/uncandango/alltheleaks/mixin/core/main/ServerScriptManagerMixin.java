package dev.uncandango.alltheleaks.mixin.core.main;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.server.ServerScriptManager;
import dev.uncandango.alltheleaks.mixin.ServerScriptManagerExtension;
import dev.uncandango.alltheleaks.mixin.core.accessor.ContextFactoryAccessor;
import net.minecraft.server.packs.PackResources;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ServerScriptManager.class, remap = false)
public abstract class ServerScriptManagerMixin extends ScriptManager implements ServerScriptManagerExtension {
	public ServerScriptManagerMixin(ScriptType t) {
		super(t);
	}

	@Inject(method = "createPackResources", at = @At(value = "NEW", target = "()Ldev/latvian/mods/kubejs/server/ServerScriptManager;"))
	private static void atl$clearContextOfOldManager(List<PackResources> original, CallbackInfoReturnable<List<PackResources>> cir){
		var server = ServerLifecycleHooks.getCurrentServer();
		if (server != null) {
			var oldManager = server.getServerResources().managers().kjs$getServerScriptManager();
			if (oldManager != null) {
				if (oldManager instanceof ServerScriptManagerExtension extension) {
					extension.clearContext();
				}
			}
		}
	}

	@Inject(method = "reloadAndCapture", at = @At("HEAD"), cancellable = true)
	private void atl$cancelReload(CallbackInfo ci){
		ci.cancel();
	}

	@Override
	public void clearContext() {
		KubeJS.PROXY.runInMainThread(() -> {
			if (this.contextFactory instanceof ContextFactoryAccessor accessor) {
				accessor.getCurrentContext().remove();
			}
		});
		if (this.contextFactory instanceof ContextFactoryAccessor accessor) {
			accessor.getCurrentContext().remove();
		}
	}
}
