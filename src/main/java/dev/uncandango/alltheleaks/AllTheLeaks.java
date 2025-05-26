package dev.uncandango.alltheleaks;

import com.mojang.logging.LogUtils;
import dev.uncandango.alltheleaks.commands.ATLCommands;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.IngredientDedupe;
import dev.uncandango.alltheleaks.leaks.IssueManager;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

import java.util.Set;

@Mod(AllTheLeaks.MOD_ID)
public final class AllTheLeaks {
	public static final String MOD_ID = "alltheleaks";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final boolean INDEV = Boolean.getBoolean("alltheleaks.indev");

	public AllTheLeaks() {
		final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		final IEventBus gameBus = MinecraftForge.EVENT_BUS;

		modBus.addListener(this::instantiateIssues);
		gameBus.addListener(this::registerReloadListener);
		if (FMLEnvironment.dist.isClient()){
			gameBus.addListener(this::registerClientCommands);
		}
		if (FMLEnvironment.dist.isDedicatedServer()) {
			gameBus.addListener(this::printNonDaemonThreads);
		}
	}

	private void instantiateIssues(FMLCommonSetupEvent event) {
		event.enqueueWork(IssueManager::initiateIssues);
		if (AllTheLeaks.INDEV) {
			IssueManager.generateIssueSummary();
		}
	}

	private void registerReloadListener(AddReloadListenerEvent event) {
		if (IngredientDedupe.INSTANCE != null) {
			event.addListener(IngredientDedupe.INSTANCE);
		}
	}

	private void registerClientCommands(RegisterClientCommandsEvent event) {
		ATLCommands.registerCommands(event.getDispatcher(), event.getBuildContext());
	}

	private void printNonDaemonThreads(ServerStoppedEvent event) {
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
					System.out.println("AllTheLeaks: Listing stuck threads...");
					threads.stream()
						.filter(t -> !t.isDaemon())
						.filter(t -> !t.getName().equals("DestroyJavaVM"))
						.forEach(AllTheLeaks::listThreads);
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
