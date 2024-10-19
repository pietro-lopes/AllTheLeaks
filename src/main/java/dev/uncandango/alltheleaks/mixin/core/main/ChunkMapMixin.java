package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.List;

@Pseudo
@Mixin(ChunkMap.class)
public class ChunkMapMixin {
    static {
        SERVER_PLAYER_HANDLE = ReflectionHelper.getFieldFromClass(ChunkMap.class, "serverPlayer", ServerPlayer.class, false);
        LIST_HANDLE = ReflectionHelper.getFieldFromClass(ChunkMap.class, "list", List.class, false);
    }
    @Unique
    private static final VarHandle SERVER_PLAYER_HANDLE;
    @Unique
    private static final VarHandle LIST_HANDLE;

    @Inject(method = "playerLoadedChunk", at = @At("TAIL"))
    private void atl$clearSmallshipsVariables(ServerPlayer arg, MutableObject<ClientboundLevelChunkWithLightPacket> mutableObject, LevelChunk arg2, CallbackInfo ci){
        if (SERVER_PLAYER_HANDLE != null) SERVER_PLAYER_HANDLE.set((ChunkMap) (Object) this, null);
        if (LIST_HANDLE != null) ((List<Entity>)LIST_HANDLE.get((ChunkMap) (Object) this)).clear();
    }
}
