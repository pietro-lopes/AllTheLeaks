package dev.uncandango.alltheleaks.mixin.core.main;

import de.maxhenkel.corpse.corelib.CachedMap;
import de.maxhenkel.corpse.entities.CorpseEntity;
import de.maxhenkel.corpse.entities.CorpseRenderer;
import de.maxhenkel.corpse.entities.DummyPlayer;
import de.maxhenkel.corpse.entities.DummySkeleton;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CorpseRenderer.class, remap = false)
public class CorpseRendererMixin implements UpdateableLevel<CorpseRenderer> {

	@Shadow
	@Final
	private CachedMap<CorpseEntity, DummyPlayer> players;

	@Shadow
	@Final
	private CachedMap<CorpseEntity, DummySkeleton> skeletons;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void registerInstance(CallbackInfo ci) {
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		if (level != null) {
			players.clear();
			skeletons.clear();
		}
	}
}
