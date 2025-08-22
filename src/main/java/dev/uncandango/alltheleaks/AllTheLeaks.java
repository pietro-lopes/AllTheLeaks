package dev.uncandango.alltheleaks;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mod(AllTheLeaks.MOD_ID)
public class AllTheLeaks {
	public static final String MOD_ID = "alltheleaks";
	public static final Logger LOGGER = LoggerFactory.getLogger("AllTheLeaks");
	public static final boolean INDEV = Boolean.getBoolean("alltheleaks.indev");

	public AllTheLeaks(IEventBus eventModBus, ModContainer modContainer) {
		//var gameBus = NeoForge.EVENT_BUS;
		// eventModBus.addListener(this::commonSetup);
		if (FMLEnvironment.dist.isClient()) {
			// gameBus.addListener(this::addDebugOSMemoryUsed);
			// gameBus.addListener(this::clientCommands);
		}
		if (FMLEnvironment.dist.isDedicatedServer()) {
			//gameBus.addListener(this::printNonDaemonThreads);
		}
	}
}
