package dev.uncandango.alltheleaks.events.client;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.commands.ATLCommands;
import dev.uncandango.alltheleaks.exceptions.ATLUnsupportedOperation;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.MemoryMonitor;
import dev.uncandango.alltheleaks.mixin.Trackable;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.report.ReportManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.DatapackLoadFailureScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.LoadingModList;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = AllTheLeaks.MOD_ID)
public class GameBusEvent {

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event){
		ReportManager.tick();
	}

	@SubscribeEvent
	public static void onWorldSelection(ScreenEvent.Opening event){
		var screen = event.getNewScreen();
		if (screen instanceof DatapackLoadFailureScreen
				|| screen instanceof DisconnectedScreen
				|| screen instanceof SelectWorldScreen) {

			int errors = ATLUnsupportedOperation.getErrorCount();
			if (errors > 0) {
				var toastcomponent = Minecraft.getInstance().getToasts();
				SystemToast.addOrUpdate(toastcomponent,
					SystemToast.SystemToastIds.WORLD_ACCESS_FAILURE,
					Component.literal("[AllTheLeaks] Errors"),
					Component.translatable("%s errors was found due to Ingredient Dedupe feature", errors));
			}
		}
	}

	@SubscribeEvent
	public static void onEntityLeaveLevelEvent(EntityLeaveLevelEvent event) {
		if (event.getEntity() instanceof RemotePlayer player) {
			Trackable.startTracking(player);
		}
	}

	@SubscribeEvent
	public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
		if (event.getConnection().isMemoryConnection()){
			MemoryMonitor.EventStatistics.WORLD_JOIN_SINGLEPLAYER.increment();
		} else {
			MemoryMonitor.EventStatistics.WORLD_JOIN_MULTIPLAYER.increment();
		}
	}

	@SubscribeEvent
	public static void onClientPlayerClone(ClientPlayerNetworkEvent.Clone event){
		Trackable.startTracking(event.getOldPlayer());
		MemoryMonitor.EventStatistics.CLIENT_PLAYER_CLONE.increment();
	}

	@SubscribeEvent
	public static void onRenderLevelUpdate(UpdateableLevel.RenderEnginesUpdated event){
		MemoryMonitor.EventStatistics.CLIENT_LEVEL_UPDATE.increment();
		Trackable.clearNullReferences();
		if (event.getLevel() != null && !ReportManager.isRegistered("min_memory_tracker")) {
			ReportManager.registerTask("min_memory_tracker",1, MemoryMonitor.Statistics::evaluateMemory);
		}
	}

	private static final boolean IS_BETTERF3_LOADED = LoadingModList.get().getModFileById("betterf3") != null;
	@SubscribeEvent
	public static void onDebugRender(CustomizeGuiOverlayEvent.DebugText event){
		if (Minecraft.getInstance().options.renderDebug && Minecraft.getInstance().player != null){
			if (IS_BETTERF3_LOADED) return;
			event.getLeft().addAll(MemoryMonitor.getFullSummary(MemoryMonitor.DEBUG_MOD_PREFIX, true));
		}
	}

	@SubscribeEvent
	public static void registerClientCommands(RegisterClientCommandsEvent event) {
		ATLCommands.registerClientCommands(event.getDispatcher(), event.getBuildContext());
	}
}
