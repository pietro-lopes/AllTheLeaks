package dev.uncandango.alltheleaks.mixin.core.accessor;

import me.desht.pneumaticcraft.common.pneumatic_armor.CommonArmorHandler;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CommonArmorHandler.class)
public interface CommonArmorHandlerAccessor {
	@Invoker("clearHandlerForPlayer")
	static void invokeClearHandlerForPlayer(Player event){};
}
