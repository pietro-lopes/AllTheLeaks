package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.sugar.Local;
import me.desht.pneumaticcraft.client.render.area.AreaRenderManager;
import me.desht.pneumaticcraft.client.render.area.AreaRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(value = AreaRenderManager.class, remap = false)
public class AreaRenderManagerMixin {
//	@Shadow
//	private Level level;
//
//	@Shadow
//	private List<AreaRenderer> cachedPositionProviderShowers;
//
//	@Shadow
//	@Final
//	private Map<BlockPos, AreaRenderer> showHandlers;
//
//	@Inject(method = "tickEnd", at = @At(value = "TAIL"))
//	private void atl$cleanUp(ClientTickEvent.Post event, CallbackInfo ci, @Local Player player) {
//		if (player == null && this.level != null) {
//			this.level = null;
//			if (this.cachedPositionProviderShowers != null) {
//				this.cachedPositionProviderShowers.clear();
//			}
//			showHandlers.clear();
//		}
//	}
}
