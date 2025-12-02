package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sonar.fluxnetworks.common.connection.ServerFluxNetwork;
import sonar.fluxnetworks.common.device.TileFluxDevice;

import java.util.ArrayList;
import java.util.Objects;

@Mixin(ServerFluxNetwork.class)
public class ServerFluxNetworkMixin {
	@ModifyExpressionValue(method = "onEndServerTick", at = @At(value = "INVOKE", target = "Lsonar/fluxnetworks/common/connection/ServerFluxNetwork;getLogicalDevices(I)Ljava/util/ArrayList;"))
	private ArrayList<TileFluxDevice> atl$returnOnlyIfShouldTick(ArrayList<TileFluxDevice> original){
		var newList = new ArrayList<TileFluxDevice>(original.size());
		for(var tile : original) {
			if (Objects.requireNonNull(tile.getLevel()).shouldTickBlocksAt(tile.getBlockPos())) {
				newList.add(tile);
			}
		}
		return newList;
	}
}
