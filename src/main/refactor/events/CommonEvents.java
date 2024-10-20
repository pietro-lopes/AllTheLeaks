package dev.uncandango.alltheleaks.events;

import dev.uncandango.alltheleaks.commands.ATLCommands;
import dev.uncandango.alltheleaks.mixin.core.accessor.NetworkManagerImplAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.RecipeRegistryJeiRecipeWrapperAccessor;
import dev.uncandango.alltheleaks.mods.Gtceu;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

@SuppressWarnings("unused")
//@Mod.EventBusSubscriber
public class CommonEvents {

    @SubscribeEvent
    public static void onLoadLevel(LevelEvent.Load event) {
        if (ModList.get().isLoaded("gtceu")) {
            if (event.getLevel().isClientSide()) {
                Gtceu.setDummyWorld(event.getLevel());
            }
        }
    }

//    @SubscribeEvent
//    public static void onUnloadLevel(LevelEvent.Unload event) {
//        var level = event.getLevel();
//        if (level.isClientSide()){
//            for (var it = StorageClient.stackWithRepresentation.iterator(); it.hasNext();){
//                var stack = it.next().get();
//                if (stack == null) {
//                    it.remove();
//                    continue;
//                };
//                var entity = stack.getEntityRepresentation();
//                if (entity != null && entity.level().isClientSide()){
//                    stack.setEntityRepresentation(null);
//                }
//            }
//        }
//    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ATLCommands.registerCommands(event.getDispatcher(), event.getBuildContext());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void clearReloadables(AddReloadListenerEvent event) {
        if (ModList.get().isLoaded("cyclopscore")) {
            RecipeRegistryJeiRecipeWrapperAccessor.atl$getRECIPE_WRAPPERS().clear();
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        NetworkManagerImplAccessor.atl$getClientReveivables().removeAll(event.getOriginal());
//        var oldPlayer = event.getOriginal();
//        if (oldPlayer.level() instanceof ServerLevelAccessor level) {
//            if (level.atl$getEntityTickList() instanceof EntityTickListAccessor tickList) {
//                tickList.atl$getPassive().remove(oldPlayer.getId(), oldPlayer);
//            }
//        }
    }
}
