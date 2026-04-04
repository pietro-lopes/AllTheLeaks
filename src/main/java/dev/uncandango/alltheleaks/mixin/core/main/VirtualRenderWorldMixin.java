package dev.uncandango.alltheleaks.mixin.core.main;

import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import dev.engine_room.flywheel.lib.util.LevelAttached;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VirtualRenderWorld.class)
public class VirtualRenderWorldMixin implements UpdateableLevel<VirtualRenderWorld> {
	@Shadow
	@Final
	protected Level level;

	@Inject(method = {
		"<init>(Lnet/minecraft/world/level/Level;IILnet/minecraft/core/Vec3i;)V",
		"<init>(Lnet/minecraft/world/level/Level;IILnet/minecraft/core/Vec3i;Ljava/lang/Runnable;)V"
	}, at = @At("TAIL"), require = 1, allow = 1, expect = 1)
	private void grabInstance(CallbackInfo ci) {
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		if (this.level != level) {
			NeoForge.EVENT_BUS.post(new LevelEvent.Unload((VirtualRenderWorld)(Object)this));
		}
	}
}
