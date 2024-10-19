package dev.uncandango.alltheleaks;

import com.mojang.logging.LogUtils;
import dev.uncandango.alltheleaks.leaks.IssueManager;
import dev.uncandango.alltheleaks.mixin.core.accessor.CitadelServerDataAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.ConductorPossessionControllerAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.CraftingTerminalHandlerAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.ExtendoGripItemAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.ThreadedParametersAccessor;
import dev.uncandango.alltheleaks.mods.Ae2;
import dev.uncandango.alltheleaks.mods.AmbientSounds;
import dev.uncandango.alltheleaks.mods.ArsNouveau;
import dev.uncandango.alltheleaks.mods.Blueskies;
import dev.uncandango.alltheleaks.mods.Create;
import dev.uncandango.alltheleaks.mods.Createaddition;
import dev.uncandango.alltheleaks.mods.DistantHorizons;
import dev.uncandango.alltheleaks.mods.FtbChunks;
import dev.uncandango.alltheleaks.mods.Gtceu;
import dev.uncandango.alltheleaks.mods.IceAndFire;
import dev.uncandango.alltheleaks.mods.ImmersiveEng;
import dev.uncandango.alltheleaks.mods.IronsSpells;
import dev.uncandango.alltheleaks.mods.Jade;
import dev.uncandango.alltheleaks.mods.Journeymap;
import dev.uncandango.alltheleaks.mods.Kubejs;
import dev.uncandango.alltheleaks.mods.Lootjs;
import dev.uncandango.alltheleaks.mods.Mekanism;
import dev.uncandango.alltheleaks.mods.Minecolonies;
import dev.uncandango.alltheleaks.mods.MinecraftClient;
import dev.uncandango.alltheleaks.mods.Pneumaticcraft;
import dev.uncandango.alltheleaks.mods.Quark;
import dev.uncandango.alltheleaks.mods.Railcraft;
import dev.uncandango.alltheleaks.mods.Tfcthermaldeposits;
import net.minecraft.Util;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

@Mod(AllTheLeaks.MOD_ID)
public final class AllTheLeaks {
	public static final String MOD_ID = "alltheleaks";
	public static final Logger LOGGER = LogUtils.getLogger();

	public AllTheLeaks() {
		final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		final IEventBus gameBus = MinecraftForge.EVENT_BUS;
//        gameBus.addListener(this::clearMaps);
		modBus.addListener(this::instantiateIssues);
//		gameBus.addListener(EventPriority.HIGHEST, this::printEntityCloneHighest);
//		gameBus.addListener(EventPriority.LOWEST, this::printEntityCloneLowest);
//		gameBus.addListener(this::printEntityCloneNormal);
	}

	private void instantiateIssues(FMLCommonSetupEvent event) {
		event.enqueueWork(IssueManager::initiateIssues);
	}

	private void printEntityCloneHighest(PlayerEvent.Clone event) {
		AllTheLeaks.LOGGER.info("(Highest) Entity is: {}", event.getOriginal());
	}

	private void printEntityCloneNormal(PlayerEvent.Clone event) {
		AllTheLeaks.LOGGER.info("(Normal) Entity is: {}", event.getOriginal());
	}

	private void printEntityCloneLowest(PlayerEvent.Clone event) {
		AllTheLeaks.LOGGER.info("(Lowest) Entity is: {}", event.getOriginal());
	}

	public void clearMaps(ServerStoppedEvent event) {

		var startTimer = Util.getNanos();
		LOGGER.info("Cleaning server side objects...");
		var modList = ModList.get();
//		if (modList.isLoaded("create")) {
//			safeCall(() -> ExtendoGripItemAccessor.setLastActiveDamageSource(null), "Cleaning Create...", "Error while cleaning Create...");
//		}
		if (modList.isLoaded("distanthorizons")) {
			safeCall(() -> ThreadedParametersAccessor.atl$setPreviousGlobalParameters(null), "Cleaning Distant Horizon...", "Error while cleaning Distant Horizon...");
		}
		if (!event.getServer().isDedicatedServer()) {
			LOGGER.info("Cleaning client side objects...");
			MinecraftClient.run();
			for (var mod : modList.getMods()) {
				switch (mod.getModId()) {
//					case "createaddition" -> safeCallCleaning(Createaddition::run, mod);
//					case "citadel" -> safeCallCleaning(() -> CitadelServerDataAccessor.atl$getDataMap().clear(), mod);
//					case "railcraft" -> safeCallCleaning(Railcraft::run, mod);
//					case "tfcthermaldeposits" -> safeCallCleaning(Tfcthermaldeposits::run, mod);
					case "gtceu" -> safeCallCleaning(() -> Gtceu.run(event.getServer()), mod);
					case "kubejs" -> safeCallCleaning(Kubejs::run, mod);
//					case "ae2" -> safeCall(Ae2::run, null, "Error while cleaning " + mod.getDisplayName() + " JEI Plugin");
					// TODO: Test if still applies
					case "quark" -> safeCallCleaning(Quark::run, mod);
//					case "ae2wtlib" -> safeCallCleaning(() -> CraftingTerminalHandlerAccessor.atl$getPlayers().clear(), mod);
					case "ars_nouveau" -> safeCallCleaning(ArsNouveau::run, mod);
					case "blue_skies" -> safeCallCleaning(Blueskies::run, mod);
					case "irons_spellbooks" -> safeCallCleaning(IronsSpells::run, mod);
					case "minecolonies" -> safeCallCleaning(Minecolonies::run, mod);
					case "railways" -> safeCallCleaning(() -> ConductorPossessionControllerAccessor.atl$setCameraStorage(null), mod);
//					case "create" -> safeCallCleaning(Create::run, mod);
					case "ftbchunks" -> safeCallCleaning(FtbChunks::run, mod);
					case "pneumaticcraft" -> safeCallCleaning(Pneumaticcraft::run, mod);
					case "mekanism" -> safeCallCleaning(Mekanism::run, mod);
					case "journeymap" -> safeCallCleaning(Journeymap::run, mod); // x
					case "jade" -> safeCallCleaning(Jade::run, mod); // x
					case "iceandfire" -> safeCallCleaning(IceAndFire::run, mod); // x
					case "ambientsounds" -> safeCallCleaning(AmbientSounds::run, mod); // x
//                    case "xaeroworldmap" -> safeCallCleaning(Xaeros::run, mod); // y
//                    case "xaerominimap" -> safeCallCleaning(XaeroMinimap::run,mod); // z
					case "distanthorizons" -> safeCallCleaning(DistantHorizons::run, mod); // y
					case "immersiveengineering" -> safeCallCleaning(ImmersiveEng::run, mod);
					case "lootjs" -> safeCallCleaning(Lootjs::run, mod);
					default -> {
					}
				}
			}
			LOGGER.info("Took {} ms to clear objects...", (int) (Util.getNanos() - startTimer) / 1000000);
			//        try {
			//            MemoryFixCommands.dumpHeap();
			//        } catch (Throwable ignored){}
		}
	}

	private static void safeCall(Runnable task, @Nullable String startMessage, String error) {
		try {
			if (startMessage != null) {
				LOGGER.debug(startMessage);
			}
			task.run();
		} catch (Throwable t) {
			AllTheLeaks.LOGGER.error(error, t.getMessage());
		}
	}

	private static void safeCallCleaning(Runnable task, IModInfo mod) {
		safeCall(task, "Cleaning " + mod.getDisplayName() + "...", "Error while cleaning " + mod.getDisplayName());
	}
}
