package dev.uncandango.alltheleaks.events.client;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.exceptions.ATLUnsupportedOperation;
import dev.uncandango.alltheleaks.report.ReportManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.DatapackLoadFailureScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
}
