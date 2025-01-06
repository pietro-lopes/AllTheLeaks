package dev.uncandango.alltheleaks;

import dev.uncandango.alltheleaks.commands.ATLCommands;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.IngredientDedupe;
import dev.uncandango.alltheleaks.leaks.IssueManager;
import dev.uncandango.alltheleaks.utils.MemoryStats;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mod(AllTheLeaks.MOD_ID)
public class AllTheLeaks {
	public static final String MOD_ID = "alltheleaks";
	public static final Logger LOGGER = LoggerFactory.getLogger("AllTheLeaks");
	public static final boolean INDEV = Boolean.getBoolean("alltheleaks.indev");

	public AllTheLeaks(IEventBus eventModBus, ModContainer modContainer) {
		var gameBus = NeoForge.EVENT_BUS;
		eventModBus.addListener(this::commonSetup);
		gameBus.addListener(this::registerReloadListener);
		if (FMLEnvironment.dist.isClient()) {
			gameBus.addListener(this::addDebugOSMemoryUsed);
			gameBus.addListener(this::clientCommands);
		}
	}

	private void commonSetup(InterModProcessEvent event) {
		event.enqueueWork(IssueManager::initiateIssues);
	}

	private void registerReloadListener(AddReloadListenerEvent event) {
		if (IngredientDedupe.INSTANCE != null) {
			event.addListener(IngredientDedupe.INSTANCE);
		}
	}

	private void clientCommands(RegisterClientCommandsEvent event) {
		ATLCommands.registerCommands(event.getDispatcher(), event.getBuildContext());
	}

	private void addDebugOSMemoryUsed(CustomizeGuiOverlayEvent.DebugText event) {
		if (MemoryStats.ENABLED) {
			event.getRight().add(4, MemoryStats.getMemoryWorkingSetSize());
		}
	}
}
