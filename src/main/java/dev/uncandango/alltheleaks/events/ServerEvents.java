package dev.uncandango.alltheleaks.events;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.commands.ATLCommands;
import dev.uncandango.alltheleaks.config.ATLProperties;
import dev.uncandango.alltheleaks.diag.server.mods.minecraft.DebugThreadsHooks;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.MemoryMonitor;
import dev.uncandango.alltheleaks.report.ReportManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static dev.uncandango.alltheleaks.events.CommonEvents.reports;

@EventBusSubscriber(modid = AllTheLeaks.MOD_ID, value = Dist.DEDICATED_SERVER)
public class ServerEvents {

	@SubscribeEvent
	public static void onServerTick(ServerTickEvent.Post event){
		ReportManager.tick();
	}

	@SubscribeEvent
	public static void registerServerCommands(RegisterCommandsEvent event) {
		ATLCommands.registerServerCommands(event.getDispatcher(), event.getBuildContext());
	}

	@SubscribeEvent
	public static void onServerStarted(ServerStartedEvent event){
		if (!ReportManager.isRegistered("min_memory_tracker")) {
			ReportManager.registerTask("min_memory_tracker",1, MemoryMonitor.Statistics::evaluateMemory);
		}
		// Simulate a stuck thread
//		Thread threadStuck = new Thread(() -> {
//			while (true) {
//				try {
//					//noinspection BusyWait
//					Thread.sleep(60000);
//				} catch (InterruptedException ignore) {
//				}
//			}
//		});
//		threadStuck.setName("AllTheLeaks Stuck Thread Example");
//		threadStuck.start();
	}

	@SubscribeEvent
	public static void printNonDaemonThreads(ServerStoppedEvent event) {
		AtomicBoolean isFirstRun = new AtomicBoolean(true);
		Thread thread = new Thread(() -> {
			while (true) {
				try {
					if (isFirstRun.get()) {
						//noinspection BusyWait
						Thread.sleep(10000);
					} else {
						//noinspection BusyWait
						Thread.sleep(30000);
					}
				} catch (InterruptedException e) {
					break;
				}
				Set<Thread> threads = Thread.getAllStackTraces().keySet();
				var nonDaemonThreads = threads.stream()
					.filter(t -> !t.isDaemon())
					.filter(t -> !t.getName().equals("DestroyJavaVM")).toList();
				if (!nonDaemonThreads.isEmpty()) {
					nonDaemonThreads.forEach(DebugThreadsHooks::prettyPrintEvent);
					if (isFirstRun.get()) {
						if (!ATLProperties.get().debugThreadsStuck) {
							AllTheLeaks.LOGGER.warn("If you did *NOT CRASH*, consider activating \"debugThreadsStuck\" at \"config/alltheleaks.json\" to find where those stuck threads are from");
						}
						if (reports > 0) {
							AllTheLeaks.LOGGER.warn("Seems like you crashed and the server got stuck, you might need to terminate the process!");
						} else {
							AllTheLeaks.LOGGER.warn("Seems like server is stuck while trying to close. Read messages above about stuck threads!");
						}

						isFirstRun.set(false);
					}
				}
			}
		});
		thread.setName("Stuck Thread Watcher");
		thread.setDaemon(true);
		thread.start();
	}
}
