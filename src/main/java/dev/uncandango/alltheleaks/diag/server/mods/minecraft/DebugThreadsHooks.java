package dev.uncandango.alltheleaks.diag.server.mods.minecraft;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import jdk.jfr.consumer.RecordedClass;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordedStackTrace;
import net.neoforged.fml.ModList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class DebugThreadsHooks {
	private static final Map<Long, String> currentEvents = new Long2ObjectOpenHashMap<>();
	private static final LongSet visitedThreads = new LongOpenHashSet();
	private static final Logger LOGGER = LoggerFactory.getLogger(DebugThreadsHooks.class.getSimpleName());

	private static final List<String> skipMethods = List.of(
		"LambdaForm$",
		"Invokers$",
		"$$Lambda"
	);

	private static final List<String> skipModules = List.of(
		"forge",
		"minecraft",
		"neoforge"
	);

	public static void track(RecordedEvent event) {
		long key = event.getValue("thread.javaThreadId");
		LOGGER.debug("Tracking event for thread \"{}\" Id={}", event.getValue("thread.javaName"), key);
		currentEvents.put(key, stringify(event.getValue("stackTrace")));
	}

	public static void untrack(RecordedEvent event) {
		long key = event.getValue("thread.javaThreadId");
		LOGGER.debug("Untracking event for thread \"{}\" Id={}", event.getValue("thread.javaName"), key);
		currentEvents.remove(key);
	}

	public static void prettyPrintEvent(Thread thread) {
		long threadId = thread.threadId();
		if (visitedThreads.contains(threadId)) {
			return;
		}
		visitedThreads.add(threadId);
		LOGGER.info("Stuck Thread: \"{}\" Id={}", thread.getName(), threadId);
		String stackTraceText = currentEvents.get(threadId);
		if (stackTraceText != null) {
			LOGGER.info("-- Stacktrace: \n{}", stackTraceText);
		} else {
			LOGGER.debug("Thread Id {} was not tracked!", threadId);
		}
	}

	private static String stringify(RecordedStackTrace stackTrace) {
		var stringBuilder = new StringBuilder();
		if (stackTrace != null) {
			try {
				Set<String> modules = new HashSet<>();
				for (var frame : stackTrace.getFrames()) {
					if (frame.isJavaFrame()) {
						RecordedClass clazz = frame.getMethod().getType();
						if (skipMethods.stream().anyMatch(str -> clazz.getName().contains(str))) {
							continue;
						}
						// 	at TRANSFORMER/minecraft@1.21.1/net.minecraft.client.KeyboardHandler.tick(KeyboardHandler.java:545)
						stringBuilder.append("\tat ");
						var classLoader = clazz.getValue("package.module.classLoader.name");
						String moduleName = clazz.getValue("package.module.name");
						if (moduleName != null && !skipModules.contains(moduleName)) {
							modules.add(moduleName);
						}
						var moduleVersion = clazz.getValue("package.module.version");
						stringBuilder.append(classLoader);
						stringBuilder.append("/");
						stringBuilder.append(moduleName);
						if (moduleVersion != null) {
							stringBuilder.append("@");
							stringBuilder.append(moduleVersion);
						}
						stringBuilder.append("/");
						stringBuilder.append(clazz.getName());
						stringBuilder.append(".");
						stringBuilder.append(frame.getMethod().getName());
						stringBuilder.append("(");
						var splits = List.of(clazz.getName().split("\\."));
						var className = splits.get(splits.size() - 1).split(Pattern.quote("$"));
						stringBuilder.append(className[0]);
						stringBuilder.append(".java:");
						stringBuilder.append(frame.getLineNumber());
						stringBuilder.append(")");
					} else {
						stringBuilder.append("\tat ");
						stringBuilder.append(frame.getMethod().toString());
					}
					stringBuilder.append("\n");
				}
				if (ModList.get() != null) {
					var susMods = modules.stream()
						.map(ModList.get()::getModFileById)
						.filter(Objects::nonNull)
						.toList();
					if (!susMods.isEmpty()) {
						stringBuilder.append("\n\tSuspecting mods:\n");
						susMods.forEach(mod -> {
							var mainMod = mod.getMods().getFirst();
							var modName = mainMod.getDisplayName();
							var modId = mainMod.getModId();
							var modVersion = mainMod.getVersion();
							var fileName = mainMod.getOwningFile().getFile().getSecureJar().getPrimaryPath().getFileName();

							stringBuilder.append("\t - ")
								.append(modName)
								.append(" (")
								.append(modId)
								.append(")\n")
								.append("\t   - version: ")
								.append(modVersion)
								.append("\n")
								.append("\t   - file: ")
								.append(fileName.toString())
								.append("\n");
						});
					}
				}
			} catch (Throwable e) {
				LOGGER.error("Something went wrong while stringifying stacktrace: {}", stackTrace);
			}
		}
		return stringBuilder.toString();
	}
}
