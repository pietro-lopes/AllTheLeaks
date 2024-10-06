package dev.uncandango.alltheleaks.mixin.core.main;

import com.minecolonies.core.compatibility.jei.JEIPlugin;
import dev.uncandango.alltheleaks.leaks.client.mods.minecolonies.Issue10302;
import mezz.jei.api.IModPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(JEIPlugin.class)
public class MineColoniesJEIPluginMixin {
	@Inject(method = "<init>", at = @At("RETURN"))
	private void grabInstance(CallbackInfo ci){
		Issue10302.MineColoniesJEIPlugin = (IModPlugin) this;
	}
}
