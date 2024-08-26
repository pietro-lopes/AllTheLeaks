package dev.uncandango.alltheleaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IGuiScreen;
import me.desht.pneumaticcraft.client.gui.pneumatic_armor.options.SearchOptions;
import me.desht.pneumaticcraft.client.pneumatic_armor.upgrade_handler.SearchClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SearchOptions.class, remap = false)
public class SearchOptionsMixin {
	@Mutable
	@Shadow
	@Final
	private Player player;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void atl$clearPlayer(IGuiScreen screen, SearchClientHandler upgradeHandler, CallbackInfo ci){
		player = null;
	}

	@WrapOperation(method = "populateGui", at = @At(value = "FIELD", target = "Lme/desht/pneumaticcraft/client/gui/pneumatic_armor/options/SearchOptions;player:Lnet/minecraft/world/entity/player/Player;"))
	private Player atl$redirectPlayer(SearchOptions instance, Operation<Player> original){
		return Minecraft.getInstance().player;
	}

	@WrapOperation(method = "openSearchGui", at = @At(value = "FIELD", target = "Lme/desht/pneumaticcraft/client/gui/pneumatic_armor/options/SearchOptions;player:Lnet/minecraft/world/entity/player/Player;"))
	private Player atl$redirectPlayer2(SearchOptions instance, Operation<Player> original){
		return Minecraft.getInstance().player;
	}
}
