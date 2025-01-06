package dev.uncandango.alltheleaks.utils;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.api.windows.ProcessMemoryCounter;
import dev.uncandango.alltheleaks.api.windows.PsApi;
import net.minecraft.Util;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;
import org.lwjgl.system.SharedLibrary;
import org.lwjgl.system.windows.Kernel32;

import java.util.Locale;

public class MemoryStats {
	public static final boolean ENABLED;
	public static long lastUpdateTime = Util.getMillis();
	public static volatile String memoryString = "OS Used Memory: Fetching...";

	static {
		var status = Platform.get() == Platform.WINDOWS;
		if (status) {
			try {
				PsApi.getLibrary();
			} catch (Throwable e) {
				AllTheLeaks.LOGGER.error("Error while instancing PsApi: {}", e.getMessage());
				status = false;
			}
		}
		ENABLED = status;
	}

	public static String getMemoryWorkingSetSize() {
		var timeNow = Util.getMillis();
		if ((timeNow - lastUpdateTime) > 1000) {
			lastUpdateTime = timeNow;
			memoryString = String.format(Locale.ROOT, "OS Used Memory: %03dMB", MemoryStats.fetchMemorySize() / 1024 / 1024);
		}
		return memoryString;
	}

	private static long fetchMemorySize() {
		try (var stack = MemoryStack.stackPush()) {
			var report = ProcessMemoryCounter.malloc(stack);
			PsApi.GetProcessMemoryInfo(Kernel32.GetCurrentProcess(), report.address(), report.sizeof());
			return report.workingSetSize();
		}
	}
}
