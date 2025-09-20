package dev.uncandango.alltheleaks;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mod(AllTheLeaks.MOD_ID)
public class AllTheLeaks {
	public static final String MOD_ID = "alltheleaks";
	public static final Logger LOGGER = LoggerFactory.getLogger("AllTheLeaks");
	public static final boolean INDEV = Boolean.getBoolean("alltheleaks.indev");

	public AllTheLeaks(IEventBus eventModBus, ModContainer modContainer) {
		// Refer to dev.uncandango.alltheleaks.events for Events
	}
}
