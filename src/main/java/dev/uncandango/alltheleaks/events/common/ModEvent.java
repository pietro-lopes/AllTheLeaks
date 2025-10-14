package dev.uncandango.alltheleaks.events.common;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.config.ATLProperties;
import dev.uncandango.alltheleaks.exceptions.ATLUnsupportedOperation;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.MemoryMonitor;
import dev.uncandango.alltheleaks.mixin.Trackable;
import dev.uncandango.alltheleaks.report.ReportManager;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
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
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.throwables.ClassAlreadyLoadedException;
import org.spongepowered.asm.mixin.transformer.Config;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.perf.Profiler;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Set;

@Mod.EventBusSubscriber(modid = AllTheLeaks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvent {

	@SubscribeEvent
	public static void onModLoadComplete(FMLLoadCompleteEvent event){
		event.enqueueWork(() -> {
			if (ATLProperties.get().ingredientDedupe){
				ReportManager.registerTask("ingame_ingredient_dedupe_errors", 300, ModEvent::reportIngameIngredientDedupeErrors);
			}
			ReportManager.registerTask("clear_memory_leak_map", 6000, Trackable::clearNullReferences);
			CrashReportCallables.registerCrashCallable("AllTheLeaks", ModEvent::generateReportForCrashReport);
			var logIntervalInTicks = ATLProperties.get().logIntervalInMinutes * 60 * 20;
			ReportManager.registerTask("passive_memory_leak_report", logIntervalInTicks, () -> MemoryMonitor.logFullSummary(AllTheLeaks.LOGGER::info));
			ReportManager.registerTask("update_leak_summary", 100, MemoryMonitor::updateLeakSummary);
			if (ATLProperties.get().memoryUsageWarningPercentage > 0) {
				ReportManager.registerTask("too_much_memory_usage", 100, MemoryMonitor::tooMuchMemoryUsage);
			}

			if (AllTheLeaks.INDEV) {
				auditMyMixinsOnly();
			}
		});
	}

	private static void auditMyMixinsOnly(){
		try {
			Class<?> MIXIN_CONFIG_CLASS = ReflectionHelper.getClass("org.spongepowered.asm.mixin.transformer.MixinConfig");
			MethodHandle unhandledMixinsMH = ReflectionHelper.getMethodFromClass(MIXIN_CONFIG_CLASS, "getUnhandledTargets", MethodType.methodType(Set.class), false);
			var config = Config.create("alltheleaks.mixins.json");
			Set<String> unhandled = (Set<String>) unhandledMixinsMH.invoke(config.getConfig());

			ILogger auditLogger = MixinService.getService().getLogger("mixin.audit");

			for (String target : unhandled) {
				try {
					auditLogger.info("Force-loading class {}", target);
					MixinService.getService().getClassProvider().findClass(target, true);
				} catch (ClassNotFoundException ex) {
					auditLogger.error("Could not force-load " + target, ex);
				}
			}

			for (String target : (Set<String>) unhandledMixinsMH.invoke(config.getConfig())) {
				ClassAlreadyLoadedException ex = new ClassAlreadyLoadedException(target + " was already classloaded");
				auditLogger.error("Could not force-load " + target, ex);
			}


			if (MixinEnvironment.getCurrentEnvironment().getOption(MixinEnvironment.Option.DEBUG_PROFILER)) {
				Profiler.printAuditSummary();
			}

		} catch (Throwable e) {
			AllTheLeaks.LOGGER.error("Error while auditing mixins", e);
		}
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

	public static int reports = 0;
	private static String generateReportForCrashReport(){
		reports++;
		if (reports % 2 == 1) {
			MemoryMonitor.runExplicitGc();
		}
		Trackable.clearNullReferences();
		//if (reports % 2 == 0) MemoryMonitor.dumpHeap();
		var sb = new StringBuilder();
		sb.append("\n");
		if (ATLProperties.get().ingredientDedupe) {
			sb.append("\t\tIngredient Dedupe Errors: ").append(ATLUnsupportedOperation.getErrorCount()).append("\n");
		}

		sb.append("\t\tEvents:\n");
		MemoryMonitor.getEventsSummary().forEach(line -> {
			sb.append("\t\t  ").append(line).append("\n");
		});

		sb.append("\t\tExplicit GC Last Run: ").append(MemoryMonitor.isExplicitGcDisabled() ? "Disabled" : (Util.getMillis() - MemoryMonitor.lastRunGc()) + "ms ago").append("\n");

		if (MemoryMonitor.isExplicitGcDisabled()) {
			sb.append("\t\tNote: Values below are not accurate due to -XX:+DisableExplicitGC arguments you are using").append("\n");
		}
		sb.append("\t\tLeaking objects:\n");
		Trackable.getSummary().forEach((baseClazz, mapCount) -> {
			if (!mapCount.isEmpty()) sb.append("\t\t  ").append(baseClazz.getSimpleName()).append(":\n");
			mapCount.forEach((innerClazz, count) -> {
				sb.append("\t\t    ").append(innerClazz.getSimpleName()).append(": ").append(count).append("\n");
			});
		});
		return sb.toString();
	}}
