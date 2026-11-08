package dev.uncandango.alltheleaks;

import dev.uncandango.alltheleaks.leaks.IssueManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

@Mod(AllTheLeaks.MOD_ID)
public final class AllTheLeaks {
	public static final String MOD_ID = "alltheleaks";
	public static final Logger LOGGER = LoggerFactory.getLogger(AllTheLeaks.class.getSimpleName());
	public static final boolean INDEV = Boolean.getBoolean("alltheleaks.indev");
	private static final Path LOCAL = FMLPaths.GAMEDIR.get().resolve("local").resolve(MOD_ID);

	public AllTheLeaks() {
		final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		final IEventBus gameBus = MinecraftForge.EVENT_BUS;

		modBus.addListener(this::instantiateIssues);
	}

	private void instantiateIssues(FMLCommonSetupEvent event) {
		event.enqueueWork(IssueManager::initiateIssues);
		if (AllTheLeaks.INDEV) {
			IssueManager.generateIssueSummary();
		}
	}

	public static Path getLocal(){
		return LOCAL;
	}
}
