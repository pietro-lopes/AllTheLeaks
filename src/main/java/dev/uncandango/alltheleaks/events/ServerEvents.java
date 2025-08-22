package dev.uncandango.alltheleaks.events;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.commands.ATLCommands;
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
	}

	@SubscribeEvent
	public static void printNonDaemonThreads(ServerStoppedEvent event) {
		Thread thread = new Thread(() -> {
			while (true) {
				try {
					//noinspection BusyWait
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					break;
				}
				Set<Thread> threads = Thread.getAllStackTraces().keySet();
				if (!threads.isEmpty()) {
					if (reports > 0) System.out.println("AllTheLeaks: Seems like you crashed, it can cause stuck threads...");
					System.out.println("AllTheLeaks: Listing stuck threads...");
					threads.stream()
						.filter(t -> !t.isDaemon())
						.filter(t -> !t.getName().equals("DestroyJavaVM"))
						.forEach(ServerEvents::listThreads);
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
	}

	private static void listThreads(Thread thread) {
		System.out.println("Stuck thread: " + thread.getName());
	}
}
