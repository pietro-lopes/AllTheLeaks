package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.uncandango.alltheleaks.config.ATLProperties;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.MemoryMonitor;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {
	@ModifyReturnValue(method = "getGameInformation", at = @At("RETURN"))
	private List<String> atl$addLeftText(List<String> original){
		if (ATLProperties.get().showSummaryOnDebugScreen) {
			original.addAll(MemoryMonitor.getFullSummary(MemoryMonitor.DEBUG_MOD_PREFIX, true));
		}
		return original;
	}
}
