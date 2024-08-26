package dev.uncandango.alltheleaks;

import dev.uncandango.alltheleaks.leaks.IssueManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mod(AllTheLeaks.MOD_ID)
public class AllTheLeaks {
	public static final String MOD_ID = "alltheleaks";
	public static final Logger LOGGER = LoggerFactory.getLogger("AllTheLeaks");

	public AllTheLeaks(IEventBus eventModBus, ModContainer modContainer) {
		var gameBus = NeoForge.EVENT_BUS;
		eventModBus.addListener(this::commonSetup);
	}

	private void commonSetup(InterModProcessEvent event) {
		event.enqueueWork(IssueManager::initiateIssues);
	}
}
