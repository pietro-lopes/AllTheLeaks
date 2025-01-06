package dev.uncandango.alltheleaks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.sun.management.HotSpotDiagnosticMXBean;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.api.windows.PsApi;
import dev.uncandango.alltheleaks.config.ATLProperties;
import dev.uncandango.alltheleaks.diag.common.mods.minecraft.DebugNativeImage;
import dev.uncandango.alltheleaks.utils.MemoryStats;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

public final class ATLCommands {

	public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
		dispatcher.register(
			Commands.literal("atl")
				.then(
					Commands.literal("run_full_gc")
						.executes(cmd -> runGc(cmd.getSource()))
				)
				.then(
					Commands.literal("clear_native_images")
						.executes(cmd -> clearNativeImages(cmd.getSource()))
				)
		);
	}

	private static int clearNativeImages(CommandSourceStack source) {
		if (!ATLProperties.get().debugNativeImage) {
			source.sendFailure(Component.literal("DebugNativeImage is disabled, activate it at config/alltheleaks.json"));
			return 0;
		}
		AtomicInteger counter = new AtomicInteger();
		DebugNativeImage.NATIVE_IMAGES_TRACKER.forEach((k, v) -> {
			var setWithEmptyRef = Collections.synchronizedSet(new HashSet<DebugNativeImage.Value>());
			v.forEach(wr -> {
				var ref = wr.imageRef().get();
				if (ref == null) {
					setWithEmptyRef.add(wr);
				}
			});
			if (!v.isEmpty() && setWithEmptyRef.size() == v.size()) {
				AllTheLeaks.LOGGER.info("Removed all NativeImages for key {}", k);
				if (k.useStbFree()) {
					STBImage.nstbi_image_free(k.pixels());
				} else {
					MemoryUtil.nmemFree(k.pixels());
				}
				setWithEmptyRef.forEach(wr -> {
					counter.getAndIncrement();
					AllTheLeaks.LOGGER.info("Printing stack trace for: {}", wr.description());
					var reachedInit = false;
					for (StackTraceElement trace : wr.stackTraceElements()) {
						if (!reachedInit) {
							reachedInit = trace.getMethodName().contains("init");
						}
						if (reachedInit) {
							System.out.println("\tat " + trace.getClassName() + "." + trace.getMethodName() +
								"(" + trace.getFileName() + ":" + trace.getLineNumber() + ")");
						}

					}
				});
				v.clear();
			}
		});
		source.sendSuccess(() -> Component.literal("Cleared " + counter.get() + " NativeImages"), true);
		return 1;
	}

	@SuppressWarnings("SameReturnValue")
	public static int runGc(CommandSourceStack source) {
		try {
			var server = ManagementFactory.getPlatformMBeanServer();
			var hotSpotDiagnosticMXBean = ManagementFactory.newPlatformMXBeanProxy(
				server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
			var gcDisabled = Boolean.parseBoolean(hotSpotDiagnosticMXBean.getVMOption("DisableExplicitGC").getValue());
			if (gcDisabled) {
				source.sendFailure(Component.literal("Explicit GC is disabled, remove arguments -XX:+DisableExplicitGC"));
				return 0;
			}
		} catch (Exception e) {
			AllTheLeaks.LOGGER.error("Error while instancing MXBean: {}", e.getMessage());
			return 0;
		}
		System.gc();
		if (MemoryStats.ENABLED) {
			PsApi.EmptyWorkingSetOfCurrentProcess();
		}
		return 1;
	}
}