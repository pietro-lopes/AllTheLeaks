package dev.uncandango.alltheleaks.mixin.core.main;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.SessionSearchTrees;
import net.minecraft.core.RegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin2 {
	@Redirect(method = "handleUpdateRecipes", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/SessionSearchTrees;updateRecipes(Lnet/minecraft/client/ClientRecipeBook;Lnet/minecraft/core/RegistryAccess$Frozen;)V"))
	private void atl$dontUpdateRecipeBook(SessionSearchTrees instance, ClientRecipeBook recipeBook, RegistryAccess.Frozen registries){
		// NO-OP
	}
}
