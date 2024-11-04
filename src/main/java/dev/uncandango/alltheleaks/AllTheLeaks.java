package dev.uncandango.alltheleaks;

import com.mojang.logging.LogUtils;
import dev.uncandango.alltheleaks.commands.ATLCommands;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.IngredientDedupe;
import dev.uncandango.alltheleaks.leaks.IssueManager;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

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
}
