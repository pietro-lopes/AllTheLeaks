package dev.uncandango.alltheleaks.mixin.core.main;

import com.minecolonies.core.compatibility.jei.JEIPlugin;
import mezz.jei.api.IModPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JEIPlugin.class)
public abstract class MineColoniesJEIPluginMixin implements IModPlugin {
	@Shadow
	private boolean recipesLoaded;

	@Inject(method = "onRuntimeUnavailable", at = @At("TAIL"))
	private void toggleRecipesLoaded(CallbackInfo ci){
		this.recipesLoaded = false;
	}
}
