package dev.uncandango.alltheleaks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.sun.management.HotSpotDiagnosticMXBean;
import dev.uncandango.alltheleaks.AllTheLeaks;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.fml.loading.FMLPaths;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class ATLCommands {

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("atl")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("memorydump"))
                        .executes(cmd -> {
                            try {
                                dumpHeap();
                                return 1;
                            } catch (Throwable e) {
                                AllTheLeaks.LOGGER.error(e.getMessage());
                                return 0;
                            }
                        }));
    }

    public static void dumpHeap() throws Throwable {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        HotSpotDiagnosticMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(
                server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
        System.gc();
        var path = FMLPaths.getOrCreateGameRelativePath(Path.of("heap_dump/")).normalize().toAbsolutePath();
        mxBean.dumpHeap(path + "/" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + ".hprof", true);
    }
}
