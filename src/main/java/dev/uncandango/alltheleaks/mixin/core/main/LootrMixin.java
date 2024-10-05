package dev.uncandango.alltheleaks.mixin.core.main;

import net.neoforged.neoforge.event.RegisterCommandsEvent;
import noobanidus.mods.lootr.common.command.CommandLootr;
import noobanidus.mods.lootr.neoforge.Lootr;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Lootr.class, remap = false)
public class LootrMixin {
	@Shadow
	public CommandLootr COMMAND_LOOTR;

	@Inject(method = "onCommands", at = @At("TAIL"))
	private void atl$clearCommandVar(RegisterCommandsEvent event, CallbackInfo ci){
		this.COMMAND_LOOTR = null;
	}
}
