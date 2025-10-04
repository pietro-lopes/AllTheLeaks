package dev.uncandango.alltheleaks.feature.common.mods.minecraft;

import com.sun.management.HotSpotDiagnosticMXBean;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.commands.ATLCommands;
import dev.uncandango.alltheleaks.exceptions.ATLIllegalState;
import dev.uncandango.alltheleaks.mixin.Trackable;
import dev.uncandango.alltheleaks.report.ReportManager;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.management.JMX;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static dev.uncandango.alltheleaks.feature.common.mods.minecraft.MemoryMonitor.EventStatistics.*;

@Issue(modId = "minecraft", issueId = "Memory Monitor" ,versionRange = "1.20.1", mixins = {"main.PlayerMixin", "main.ChunkAccessMixin", "main.LevelMixin", "main.MinecraftServerMixin"})
public class MemoryMonitor {
	private static final boolean EXPLICIT_GC_DISABLED;
	private static final AtomicLong LAST_RUN_GC = new AtomicLong(Util.getMillis());
	private static final List<String> CACHED_SUMMARY = new ArrayList<>();
	public static final String DEBUG_MOD_PREFIX = ChatFormatting.GREEN + "[AllTheLeaks] " + ChatFormatting.RESET;

	static {
		boolean explicitGcDisabled;
		try {
			var server = ManagementFactory.getPlatformMBeanServer();
			var mxBean = ManagementFactory.newPlatformMXBeanProxy(
				server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
			explicitGcDisabled = Boolean.parseBoolean(mxBean.getVMOption("DisableExplicitGC").getValue());
		} catch (Exception e){
			AllTheLeaks.LOGGER.error("Error while instancing MXBean: {}", e.getMessage());
			explicitGcDisabled = true;
		}
		EXPLICIT_GC_DISABLED = explicitGcDisabled;
	}

	public static boolean runExplicitGc(){
		if (!isExplicitGcDisabled()){
			System.gc();
		} else {
			if (!diagnosticGcRun()) {
				AllTheLeaks.LOGGER.warn("Tried to run explicit GC but it was disabled.");
				return false;
			}
		}
		LAST_RUN_GC.set(Util.getMillis());
		return true;
	}

	public static boolean diagnosticGcRun(){
		try {
			var server = ManagementFactory.getPlatformMBeanServer();
			var diagnostic = ObjectName.getInstance("com.sun.management:type=DiagnosticCommand");
			var beanProxy = JMX.newMXBeanProxy(server, diagnostic, DiagnosticGcRun.class);
			beanProxy.gcRun();
		} catch (Exception e){
			AllTheLeaks.LOGGER.warn("Failed to run gc with diagnostic proxy: {}", e.getMessage());
			return false;
		}
		return true;
	}

	public static void dumpHeap(){
		try {
			var server = ManagementFactory.getPlatformMBeanServer();
			var mxBean = ManagementFactory.newPlatformMXBeanProxy(
				server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
			var path = FMLPaths.getOrCreateGameRelativePath(Path.of("heap_dump/")).normalize().toAbsolutePath();
			var filePath = path + "/" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + ".hprof";
			mxBean.dumpHeap(filePath, true);
		} catch (Exception e){
			AllTheLeaks.LOGGER.error("Error while creating heapdump: {}", e.getMessage());
		}
	}

	public static List<String> getEventsSummary(){
		List<String> lines = new ArrayList<>();
		var worldJoinSingleplayerCount = MemoryMonitor.EventStatistics.WORLD_JOIN_SINGLEPLAYER.getCount();
		var worldJoinMultiplayerCount = MemoryMonitor.EventStatistics.WORLD_JOIN_MULTIPLAYER.getCount();
		var worldJoinTotal = worldJoinSingleplayerCount + worldJoinMultiplayerCount;
		if (worldJoinTotal > 0) {
			lines.add("World join: " + worldJoinTotal);
			lines.add("  Singleplayer: " + worldJoinSingleplayerCount);
			lines.add("  Multiplayer: " + worldJoinMultiplayerCount);
		}
		var clientLevelUpdate = CLIENT_LEVEL_UPDATE.getCount();
		if (clientLevelUpdate > 0) lines.add("Client Level Updates: " + clientLevelUpdate);
		var clientPlayerClone = CLIENT_PLAYER_CLONE.getCount();
		if (clientPlayerClone > 0) lines.add("Client Player Clone: " + clientPlayerClone);
		var serverPlayerClone = SERVER_PLAYER_CLONE.getCount();
		if (serverPlayerClone > 0) lines.add("Server Player Clone: " + serverPlayerClone);
		var noRemovalReasonErrorCount = ATLIllegalState.TYPE.NO_REMOVAL_REASON.getErrorCount();
		if (noRemovalReasonErrorCount > 0) lines.add("  Invalid Cloned Players: " + noRemovalReasonErrorCount);
		var serverPlayerLogout = SERVER_PLAYER_LOGOUT.getCount();
		if (serverPlayerLogout > 0) lines.add("Server Player Logout: " + serverPlayerLogout);
		var serverStopCount = SERVER_STOP.getCount();
		if (serverStopCount > 0) lines.add("Server Stopped: " + serverStopCount);

		return lines;
	}

	public static void logFullSummary(Consumer<String> logger){
		var events = getEventsSummary();
		logger.accept("Listing events...");
		events.forEach(logger);
		logger.accept("Listing memory leaks so far...");
		getFullSummary("", false).forEach(logger);
	}

	public static String getMemoryStatistics(){
		var base = MemoryMonitor.Statistics.getMinMemoryInMb();
		if (base == 0) return "Waiting to stabilize [" + MemoryMonitor.Statistics.getStableCount() + "/" + MemoryMonitor.Statistics.getStableThreshold() + "]";
		var current = MemoryMonitor.Statistics.getCurrentMinMemoryInMb();
		var diff = current - base;
		return "B: " + base + "MB / C: " + current + "MB / Diff: +" + diff + "MB";
	}

	public static boolean isExplicitGcDisabled(){
		return EXPLICIT_GC_DISABLED;
	}

	public static long lastRunGc(){
		return LAST_RUN_GC.get();
	}

	public static List<String> getFullSummary(String prefix, boolean padding){
		List<String> lines = new ArrayList<>();
		if (padding) lines.add(" ");
		lines.add(prefix + getMemoryStatistics());
		var formattedSummary = getFormattedSummary(prefix);
		if (formattedSummary.isEmpty()) {
			lines.add(prefix + "No memory leak detected!");
		} else {
			if (padding) {
				lines.add(prefix + "Memory Leaks detected: (" + ChatFormatting.GREEN + "/atl force_refresh" + ChatFormatting.RESET + " to update)");
			} else {
				lines.add(prefix + "Memory Leaks detected: (/atl force_refresh to update)");
			}
			lines.addAll(formattedSummary);
		}
		return lines;
	}

	public static List<String> getFormattedSummary(String prefix){
		if (CACHED_SUMMARY.isEmpty() || prefix.isEmpty()) return CACHED_SUMMARY;
		return CACHED_SUMMARY.stream().map(line -> prefix + line).toList();
	}

	public static void updateLeakSummary(){
		CACHED_SUMMARY.clear();
		Trackable.getSummary().forEach((baseClazz, summaryMap) -> {
			if (summaryMap.isEmpty()) return;
			CACHED_SUMMARY.add("| " + baseClazz.getSimpleName() + ":");
			summaryMap.forEach((innerClazz, count) -> {
				var module = innerClazz.getModule();
				if (module != null) {
					CACHED_SUMMARY.add("|- " + innerClazz.getSimpleName() + " (" + module.getName() + "): " + count);
				} else {
					CACHED_SUMMARY.add("|- " + innerClazz.getSimpleName() + ": " + count);
				}
			});
		});
	}

	public static void tooMuchMemoryUsage() {
		double percentUsage = (double)MemoryMonitor.Statistics.getCurrentMinMemoryInMb() / MemoryMonitor.Statistics.getMaxMemoryInMb();
		if (percentUsage > 0.90) {
			CommandSourceStack source = null;
			if (FMLEnvironment.dist.isClient()) {
				var player = Minecraft.getInstance().player;
				if (player != null) {
					source = player.createCommandSourceStack();
				}
			} else {
				var server = ServerLifecycleHooks.getCurrentServer();
				if (server != null) {
					source = server.createCommandSourceStack();
				}
			}
			if (source == null) return;
			source.sendSystemMessage(Component.literal("You reached 90% memory usage, showing leaking objects so far...").withStyle(ChatFormatting.RED));
			source.sendSystemMessage(Component.literal(getMemoryStatistics()));
			ATLCommands.checkLeaking(source, false);
			ReportManager.stop("too_much_memory_usage");
		}
	}

	public enum EventStatistics {
		CLIENT_LEVEL_UPDATE(new AtomicInteger()),
		WORLD_JOIN_SINGLEPLAYER(new AtomicInteger()),
		WORLD_JOIN_MULTIPLAYER(new AtomicInteger()),
		CLIENT_PLAYER_CLONE(new AtomicInteger()),
		SERVER_PLAYER_CLONE(new AtomicInteger()),
		SERVER_PLAYER_LOGOUT(new AtomicInteger()),
		SERVER_STOP(new AtomicInteger());

		private final AtomicInteger count;

		EventStatistics(AtomicInteger count) {
			this.count = count;
		}

		public void increment(){
			this.count.incrementAndGet();
		}

		public int getCount(){
			return this.count.get();
		}
	}

	public interface DiagnosticGcRun {
		String gcRun();
	}

	public static class Statistics {
		private static final long maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024;
		private static long minMemory = 0;
		private static long currentMinMemory = 0;
		private static long currentUsedMemory = 0;
		private static int stableCount = 0;
		private static final int stableTicksThreshold = 10;

		public static void evaluateMemory() {
			if (FMLEnvironment.dist.isClient()) {
				if (Minecraft.getInstance().isPaused() || Minecraft.getInstance().level == null) return;
			}
			var used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			if (currentUsedMemory > used) {
				if (stableCount < stableTicksThreshold) {
					var diff = currentMinMemory > 0 ? 1 - ((double)used/currentMinMemory) : 1;
					if (Math.abs(diff) > 0.02) {
						stableCount = 0;
						// AllTheLeaks.LOGGER.info("Stable count was reset with diff of {}", diff);
					} else {
						stableCount++;
						// AllTheLeaks.LOGGER.info("Stable count is now {} with diff of {}", stableCount, diff);
					}
				}
				currentMinMemory = used;
				if ((minMemory == 0 && stableCount >= stableTicksThreshold) || currentMinMemory < minMemory) {
					minMemory = currentMinMemory;
				}
			}
			currentUsedMemory = used;
		}

		public static long getMinMemoryInMb() {
			return minMemory / 1024 / 1024;
		}

		public static long getStableCount(){
			return stableCount;
		}

		public static long getMaxMemoryInMb() {
			return maxMemory;
		}

		public static long getStableThreshold(){
			return stableTicksThreshold;
		}

		public static void reset(){
			minMemory = 0;
			stableCount = 0;
		}

		public static long getCurrentMinMemoryInMb() {
			return currentMinMemory / 1024 / 1024;
		}
	}
}
