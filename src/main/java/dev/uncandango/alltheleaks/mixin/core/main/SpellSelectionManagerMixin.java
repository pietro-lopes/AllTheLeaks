package dev.uncandango.alltheleaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SpellSelectionManager.class, remap = false)
public class SpellSelectionManagerMixin {
	@Mutable
	@Shadow
	@Final
	private Player player;

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lio/redspace/ironsspellbooks/api/magic/SpellSelectionManager;init(Lnet/minecraft/world/entity/player/Player;)V"))
	private void atl$clearPlayer(Player player, CallbackInfo ci){
		this.player = null;
	}

	@WrapOperation(method = {"init", "makeLocalSelection", "setSpellSelection"}, at = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/api/magic/SpellSelectionManager;player:Lnet/minecraft/world/entity/player/Player;"))
	private Player atl$replacePlayer(SpellSelectionManager instance, Operation<Player> original){
		return Minecraft.getInstance().player;
	}
}
