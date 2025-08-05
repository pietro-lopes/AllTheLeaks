package dev.uncandango.alltheleaks.feature.common.mods.minecraft;

import com.sun.management.HotSpotDiagnosticMXBean;
import dev.uncandango.alltheleaks.AllTheLeaks;
import net.minecraft.Util;

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryMonitor {
	private static final boolean EXPLICIT_GC_DISABLED;
	private static final AtomicLong LAST_RUN_GC = new AtomicLong(Util.getMillis());
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

	public static boolean isExplicitGcDisabled(){
		return EXPLICIT_GC_DISABLED;
	}

	public static long lastRunGc(){
		return LAST_RUN_GC.get();
	}
}
