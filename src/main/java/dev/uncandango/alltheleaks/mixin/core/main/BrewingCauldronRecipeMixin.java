package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import mod.motivationaldragon.potionblender.recipes.BrewingCauldronRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;

@Mixin(value = BrewingCauldronRecipe.class, remap = false)
public class BrewingCauldronRecipeMixin {

	@CompatibleHashes(values = {1210629074})
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z"))
	private boolean atl$cancelLoop(Iterator instance){
		return false;
	}
}
