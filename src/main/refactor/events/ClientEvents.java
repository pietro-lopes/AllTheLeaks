package dev.uncandango.alltheleaks.events;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.mixin.core.accessor.ClientLevelAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.CraftingTerminalHandlerAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.EntityTickListAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.OpenGuideHotkeyAccessor;
import dev.uncandango.alltheleaks.mods.FtbLibrary;
import dev.uncandango.alltheleaks.mods.Gtceu;
import dev.uncandango.alltheleaks.utils.StorageClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

@SuppressWarnings("unused")
//@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onPlayerClone(ClientPlayerNetworkEvent.Clone event) {
        var oldPlayer = event.getOldPlayer();
        if (event.getOldPlayer().level() instanceof ClientLevelAccessor level) {
            if (level.atl$getTickingEntities() instanceof EntityTickListAccessor tickList) {
                tickList.atl$getPassive().remove(oldPlayer.getId(), oldPlayer);
            }
        }
        if (ModList.get().isLoaded("gtceu")){
            Gtceu.updateModularUis(event.getNewPlayer());
        }
        if (ModList.get().isLoaded("ftblibrary")){
            FtbLibrary.setPrevScreen(Minecraft.getInstance().screen);
        }
        if (ModList.get().isLoaded("ae2wtlib")){
            CraftingTerminalHandlerAccessor.atl$getPlayers().remove(event.getOldPlayer());
        }
    }

    @SubscribeEvent
    public static void onUnloadLevel(LevelEvent.Unload event) {
        var level = event.getLevel();
        if (level.isClientSide()){
            for (var it = StorageClient.stackWithRepresentation.iterator(); it.hasNext();){
                var stack = it.next().get();
                if (stack == null) {
                    it.remove();
                    continue;
                };
                var entity = stack.getEntityRepresentation();
                if (entity != null && entity.level().isClientSide()){
                    stack.setEntityRepresentation(null);
                    AllTheLeaks.LOGGER.debug("Clearing stack {} with client entity {}", stack, entity);
                }
            }
            if (ModList.get().isLoaded("ae2")){
                OpenGuideHotkeyAccessor.atl$setLastStack(ItemStack.EMPTY);
            }
        }
    }

//    @SubscribeEvent
//    public static void onLoadLevel(LevelEvent.Load event) {
//        var level = event.getLevel();
//        if (level.isClientSide()){
//            if (ModList.get().isLoaded("gtceu")){
//                Gtceu.updateModularUis(Minecraft.getInstance().player);
//            }
//        }
//    }
}
