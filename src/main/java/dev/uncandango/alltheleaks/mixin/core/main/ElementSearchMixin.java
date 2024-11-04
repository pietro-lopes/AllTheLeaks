package dev.uncandango.alltheleaks.mixin.core.main;

import mezz.jei.gui.ingredients.IListElementInfo;
import mezz.jei.gui.search.ElementSearch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ElementSearch.class, remap = false)
public class ElementSearchMixin {
	@Inject(method = "add", at = @At("HEAD"), cancellable = true)
	private void atl$skipInvisible(IListElementInfo<?> info, CallbackInfo ci){
		if (!info.getElement().isVisible()) ci.cancel();
	}
}
