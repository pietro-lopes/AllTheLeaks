package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.desht.pneumaticcraft.common.block.entity.utility.AerialInterfaceBlockEntity;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AerialInterfaceBlockEntity.class)
public class AerialInterfaceBlockEntityMixin {

	@WrapWithCondition(method = "clearRemoved", at = @At(value = "INVOKE", target = "Lnet/neoforged/bus/api/IEventBus;register(Ljava/lang/Object;)V"))
	private boolean registerOnServerOnly(IEventBus instance, Object o){
		return !Minecraft.getInstance().isSameThread();
	}
}
