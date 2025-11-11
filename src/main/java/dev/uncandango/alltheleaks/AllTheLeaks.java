package dev.uncandango.alltheleaks;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;


@Mod(AllTheLeaks.MOD_ID)
public class AllTheLeaks {
	public static final String MOD_ID = "alltheleaks";
	public static final Logger LOGGER = LoggerFactory.getLogger("AllTheLeaks");
	public static final boolean INDEV = Boolean.getBoolean("alltheleaks.indev");
	private static final Path LOCAL = FMLPaths.GAMEDIR.get().resolve("local").resolve(MOD_ID);

	public AllTheLeaks(IEventBus eventModBus, ModContainer modContainer) {
		// Refer to dev.uncandango.alltheleaks.events for Events
	}

	public static Path getLocal(){
		return LOCAL;
	}
}
