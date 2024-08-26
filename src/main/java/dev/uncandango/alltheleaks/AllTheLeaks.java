package dev.uncandango.alltheleaks;

import com.mojang.logging.LogUtils;
import dev.uncandango.alltheleaks.leaks.FakePlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(AllTheLeaks.MOD_ID)
public class AllTheLeaks {
    public static final String MOD_ID = "alltheleaks";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static boolean debugMode = false;

    public AllTheLeaks(IEventBus eventModBus, ModContainer modContainer) {
        var eventBus = NeoForge.EVENT_BUS;
//        eventBus.addListener(Scheduler.INSTANCE::onShutdown);

        if (debugMode) {
            // Fake Player does not handle disconnection properly and leaks
            eventBus.addListener(FakePlayer::levelTick);
        }
    }


//    private static void playerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
//        ClassTracker.LEAKING.add(ValidClass.PLAYER, event.getEntity());
//    }
//
//    private static void shutdownServer(ServerStoppingEvent event) {
//        ClassTracker.LEAKING.add(ValidClass.SERVER, event.getServer());
//    }
//
//    private static void closedServer(ServerStoppedEvent event) {
//        if (heapDumpScheduledOnServerShutdown) {
//            heapDumpScheduledOnServerShutdown = false;
//            LDTCommands.dumpHeap();
//        }
//    }
//
//    private static void unloadChunk(ChunkEvent.Unload event) {
//        ClassTracker.LEAKING.add(ValidClass.CHUNK, event.getChunk());
//    }
//
//    public static void unloadLevel(LevelEvent.Unload event) {
//        ClassTracker.LEAKING.add(ValidClass.LEVEL, event.getLevel());
//    }
//
//    public static void registerCommands(RegisterCommandsEvent event) {
//        LDTCommands.registerCommands(event.getDispatcher(), event.getBuildContext());
//    }
//
//    private static void registerClientCommands(RegisterClientCommandsEvent event) {
//        LDTCommands.registerClientCommands(event.getDispatcher(), event.getBuildContext());
//    }
//
//    public enum ValidClass {
//        CHUNK("net.minecraft.world.level.chunk.ChunkAccess"),
//        LEVEL("net.minecraft.client.multiplayer.ClientLevel", "net.minecraft.server.level.ServerLevel"),
//        SERVER("net.minecraft.server.dedicated.DedicatedServer", "net.minecraft.client.server.IntegratedServer", "net.minecraft.gametest.framework.GameTestServer"),
//        PLAYER("net.minecraft.client.player.AbstractClientPlayer", "net.minecraft.server.level.ServerPlayer");
//
//        private final Lazy<Set<Class<?>>> classes;
//
//        ValidClass(String... classNames) {
//            this.classes = Lazy.of(() -> Stream.of(classNames).map(Utils::safeGetClass).filter(Objects::nonNull).collect(Collectors.toSet()));
//        }
//
//        public Set<Class<?>> getClasses() {
//            return classes.get();
//
//    }
}
