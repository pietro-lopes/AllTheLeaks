package dev.uncandango.alltheleaks.mixin.core.plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.uncandango.alltheleaks.feature.server.mods.minecraft.DebugThreadsHooks;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingStream;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// This class is at mixin to be isolated from other class loading and avoid deadlocks
public class DebugThreadsStuck {
	private static final Logger LOGGER = LoggerFactory.getLogger(DebugThreadsStuck.class.getSimpleName());

	static {
		if (isDebugEnabled()) {
			Thread jfrThread = new Thread(() -> {
				try (var jfrRecording = new RecordingStream()) {
					jfrRecording.enable("jdk.ThreadStart").withStackTrace();
					jfrRecording.enable("jdk.ThreadEnd");

					jfrRecording.onEvent("jdk.ThreadStart", DebugThreadsStuck::logThreadStarted);
					jfrRecording.onEvent("jdk.ThreadEnd", DebugThreadsStuck::logThreadEnded);
					jfrRecording.start();
					try {
						jfrRecording.awaitTermination();
					} catch (InterruptedException ignore) {
					}
				}
			});
			jfrThread.setDaemon(true);
			jfrThread.setName("AllTheLeaks JFR Thread");
			jfrThread.start();
		}
	}

	public static boolean isDebugEnabled() {
		Path path = FMLPaths.CONFIGDIR.get().resolve("alltheleaks.json");
		Gson GSON = new GsonBuilder().setLenient().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
		if (!path.toFile().exists()) {
			return false;
		}
		try (var reader = Files.newBufferedReader(path)) {
			var properties = GSON.fromJson(reader, JsonObject.class);
			if (properties.has("debugThreadsStuck")) {
				return properties.get("debugThreadsStuck").getAsBoolean();
			} else return false;
		} catch (IOException e) {
			LOGGER.error("Failed to load config file", e);
			return false;
		}
	}

	public static void start() {
	}

	public static void logThreadStarted(RecordedEvent event) {
		DebugThreadsHooks.track(event);
	}

	public static void logThreadEnded(RecordedEvent event) {
		DebugThreadsHooks.untrack(event);
	}
}
