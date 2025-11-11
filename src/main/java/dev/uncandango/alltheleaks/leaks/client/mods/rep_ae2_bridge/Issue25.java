package dev.uncandango.alltheleaks.leaks.client.mods.rep_ae2_bridge;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.unfamily.repae2bridge.block.entity.RepAE2BridgeBlockEntity;

import java.lang.invoke.VarHandle;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;

@Issue(issueId = "#25", modId = "rep_ae2_bridge", versionRange = "[1.0.0.0.0,1.4.0.0.1)")
public class Issue25 {
    public static final VarHandle ACTIVE_BRIDGES;

    static {
        ACTIVE_BRIDGES = ReflectionHelper.getFieldFromClass(
                RepAE2BridgeBlockEntity.class, "activeBridges", Set.class, true);
    }

    public Issue25() {
        var gameBus = NeoForge.EVENT_BUS;
        gameBus.addListener(this::clearClientSideBridges);
    }

    private void clearClientSideBridges(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {

            var server = ServerLifecycleHooks.getCurrentServer();
            var level = event.getLevel();
            if (server == null) {
                clearBridges(event.getLevel());
            } else {
                // NO CME PLS
                try {
                    server.executeIfPossible(() -> clearBridges(level));
                } catch (RejectedExecutionException ignore) {
                    clearBridges(event.getLevel());
                }
            }
        }
    }

    private void clearBridges(LevelAccessor level) {
        ((Set<BlockEntity>) ACTIVE_BRIDGES.get()).removeIf(be -> be.getLevel() == level);
    }
}
