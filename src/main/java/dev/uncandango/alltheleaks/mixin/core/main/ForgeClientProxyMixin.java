package dev.uncandango.alltheleaks.mixin.core.main;

import com.seibel.distanthorizons.core.api.internal.ClientApi;
import com.seibel.distanthorizons.core.wrapperInterfaces.world.IClientLevelWrapper;
import com.seibel.distanthorizons.forge.ForgeClientProxy;
import dev.uncandango.alltheleaks.AllTheLeaks;
import loaderCommon.forge.com.seibel.distanthorizons.common.wrappers.world.ClientLevelWrapper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Pseudo
@Mixin(value = ForgeClientProxy.class, remap = false)
public class ForgeClientProxyMixin {

    @Unique
    private LazyOptional<Boolean> atl$fixedEvent = LazyOptional.of(this::atl$checkIfFixed);

    @Inject(method = "clientLevelUnloadEvent", at = @At("HEAD"), cancellable = true)
    void atl$cancelWrongMethod(LevelEvent.Load event, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "clientChunkUnloadEvent", at = @At("HEAD"), cancellable = true)
    void atl$cancelChunkSave(ChunkEvent.Unload event, CallbackInfo ci) {
        ci.cancel();
    }

    @Unique
    @SubscribeEvent
    public void atl$clientLevelUnloadEvent(LevelEvent.Unload event) {
        if (atl$fixedEvent.orElse(false)) return;
        AllTheLeaks.LOGGER.info("level unload");
        LevelAccessor level = event.getLevel();
        if (!(level instanceof ClientLevel clientLevel)) {
            return;
        }

        IClientLevelWrapper clientLevelWrapper = ClientLevelWrapper.getWrapper(clientLevel);
        ClientApi.INSTANCE.clientLevelUnloadEvent(clientLevelWrapper);
    }

    @Unique
    private boolean atl$checkIfFixed() {
        var unloadMethod = Arrays.stream(this.getClass().getDeclaredMethods()).filter(method -> method.getName().contains("clientLevelUnloadEvent")).findFirst();
        if (unloadMethod.isPresent()) {
            for (var params : unloadMethod.get().getParameterTypes()) {
                if (params == LevelEvent.Unload.class) {
                    return true;
                }
            }
        }
        return false;
    }
}
