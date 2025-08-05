package dev.uncandango.alltheleaks.events.common;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.config.ATLProperties;
import dev.uncandango.alltheleaks.exceptions.ATLUnsupportedOperation;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.MemoryMonitor;
import dev.uncandango.alltheleaks.report.ReportManager;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.CrashReportCallables;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = AllTheLeaks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvent {

	@SubscribeEvent
	public static void onModLoadComplete(FMLLoadCompleteEvent event){
		event.enqueueWork(() -> {
			if (ATLProperties.get().ingredientDedupe){
				ReportManager.registerTask("ingame_ingredient_dedupe_errors", 300, ModEvent::reportIngameIngredientDedupeErrors);
				CrashReportCallables.registerCrashCallable("AllTheLeaks", ModEvent::generateReportForCrashReport);
			}
		});
	}

	private static final ClickEvent LINK_TO_REPORT = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/pietro-lopes/AllTheLeaks/issues/5");
	private static final HoverEvent HOVER_LINK = new HoverEvent(HoverEvent.Action.SHOW_TEXT,Component.literal("Click here").withStyle(ChatFormatting.GREEN));
	private static final Component REPORT_TO_DEV = Component.literal("[report to developer]").withStyle(style -> style.withClickEvent(LINK_TO_REPORT).withHoverEvent(HOVER_LINK).withColor(ChatFormatting.GREEN));
	private static void reportIngameIngredientDedupeErrors(){
		if (FMLEnvironment.dist.isClient()) {
			var player = Minecraft.getInstance().player;
			if (player != null){
				var count = ATLUnsupportedOperation.getUnreportedErrorCount();
				if (count > 0) {
					var message = Component.translatable("[AllTheLeaks] There are %s errors related to Ingredient Dedupe, check logs for more info and %s.", count, REPORT_TO_DEV).withStyle(ChatFormatting.RED);
					player.sendSystemMessage(message);
				}
			}
		} else {
			var server = ServerLifecycleHooks.getCurrentServer();
			if (server != null) {
				var count = ATLUnsupportedOperation.getUnreportedErrorCount();
				if (count > 0) {
					AllTheLeaks.LOGGER.warn("[AllTheLeaks] There are {} errors related to Ingredient Dedupe, check logs for more info and report to {}.", count, "https://github.com/pietro-lopes/AllTheLeaks/issues/5");
				}
			}
		}
	}

	private static String generateReportForCrashReport(){
		var sb = new StringBuilder();
		sb.append("\n");
		if (ATLProperties.get().ingredientDedupe) {
			sb.append("\t\tIngredient Dedupe Errors: ").append(ATLUnsupportedOperation.getErrorCount()).append("\n");
		}
		sb.append("\t\tExplicit GC Last Run: ").append(MemoryMonitor.isExplicitGcDisabled() ? "Disabled" : (Util.getMillis() - MemoryMonitor.lastRunGc()) + "ms ago").append("\n");
		sb.append("\t\tEvents:\n");
		sb.append("\t\t\tWorld join: ").append(3).append("\n");
		sb.append("\t\t\t\tSingleplayer: ").append(1).append("\n");
		sb.append("\t\t\t\tMultiplayer: ").append(2).append("\n");
		sb.append("\t\t\tDimension change: ").append(2).append("\n");
		sb.append("\t\t\tClient Player Respawn: ").append(3).append("\n");
		sb.append("\t\t\tServer Player Respawn: ").append(4).append("\n");
		sb.append("\t\tLeaking objects:\n");
		sb.append("\t\t\tLocalPlayer: ").append(1).append("\n");
		sb.append("\t\t\tRemotePlayer: ").append(2).append("\n");
		sb.append("\t\t\tLevelChunk: ").append(3).append("\n");
		sb.append("\t\t\tImposterProtoChunk: ").append(4).append("\n");
		sb.append("\t\t\tIntegratedServer: ").append(5).append("\n");
		sb.append("\t\t\tServerLevel: ").append(6).append("\n");
		sb.append("\t\t\tClientLevel: ").append(7).append("\n");
		return sb.toString();
	}}
