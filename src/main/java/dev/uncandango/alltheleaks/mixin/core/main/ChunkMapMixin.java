package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.leaks.common.mods.smallships.UntrackedIssue001;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(ChunkMap.class)
public class ChunkMapMixin {

    @Inject(method = "playerLoadedChunk", at = @At("TAIL"))
    private void atl$clearSmallshipsVariables(ServerPlayer arg, MutableObject<ClientboundLevelChunkWithLightPacket> mutableObject, LevelChunk arg2, CallbackInfo ci){
		try {
			UntrackedIssue001.clearSmallshipsVariables((ChunkMap) (Object) this);
		} catch (ExceptionInInitializerError ignored) {

		}
    }
}
