package dev.uncandango.alltheleaks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.uncandango.alltheleaks.AllTheLeaks;
import net.minecraft.util.GsonHelper;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ATLProperties {
	private static final Gson GSON = new GsonBuilder().setLenient().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
	private static final Path path = FMLPaths.CONFIGDIR.get().resolve("alltheleaks.json");
	private static final JsonObject defaultProperties = getDefaultProperties();
	private static ATLProperties INSTANCE;
	private static JsonObject properties;
	public boolean ingredientDedupe;
	public boolean debugItemStackModifications;
	public boolean debugNativeImage;
	public boolean disableSearchTree;
	public int logIntervalInMinutes;
	public boolean showSummaryOnDebugScreen;
	public int version;
	public int memoryUsageWarningPercentage;
	public boolean debugChunkLoading;
	public boolean debugThreadsStuck;
	public boolean disableEternalStarlightProgress;
	public boolean skipTickingUnloadedFluxNetworks;

	private ATLProperties() {
		load();
	}

	public static void save() {
		if (properties == null) {
			properties = defaultProperties.deepCopy();
		}
		try (var writer = Files.newBufferedWriter(path)) {
			defaultProperties.asMap().forEach((key, val) -> {
				properties.asMap().putIfAbsent(key, val);
			});
			GSON.toJson(properties, writer);
		} catch (IOException e) {
			AllTheLeaks.LOGGER.error("Failed to save config file", e);
		}
	}

	public static ATLProperties get() {
		if (INSTANCE == null) {
			INSTANCE = new ATLProperties();
		}
		return INSTANCE;
	}

	private static JsonObject getDefaultProperties() {
		var properties = new JsonObject();
		properties.addProperty("ingredientDedupe", false);
		properties.addProperty("debugItemStackModifications", false);
		properties.addProperty("version", 1);
		properties.addProperty("debugNativeImage", false);
		properties.addProperty("disableSearchTree", false);
		properties.addProperty("logIntervalInMinutes", 10);
		properties.addProperty("showSummaryOnDebugScreen", true);
		properties.addProperty("memoryUsageWarningPercentage", 90);
		properties.addProperty("debugChunkLoading", false);
		properties.addProperty("debugThreadsStuck", false);
		properties.addProperty("disableEternalStarlightProgress", false);
		properties.addProperty("skipTickingUnloadedFluxNetworks", false);
		// skipTickingUnloadedFluxNetworks
		return properties;
	}

	public void load() {
		if (!path.toFile().exists()) {
			save();
		}
		try (var reader = Files.newBufferedReader(path)) {
			properties = GSON.fromJson(reader, JsonObject.class);
			version = GsonHelper.getAsInt(properties, "version", 0);
			if (version == 0) {
				// Force it to false because at some point we shipped it as true
				this.ingredientDedupe = false;
			} else {
				this.ingredientDedupe = GsonHelper.getAsBoolean(properties, "ingredientDedupe", false);
			}
			this.debugItemStackModifications = GsonHelper.getAsBoolean(properties, "debugItemStackModifications", false);
			this.debugNativeImage = GsonHelper.getAsBoolean(properties, "debugNativeImage", false);
			this.disableSearchTree = GsonHelper.getAsBoolean(properties, "disableSearchTree", false);
			this.logIntervalInMinutes = GsonHelper.getAsInt(properties, "logIntervalInMinutes", 10);
			this.showSummaryOnDebugScreen = GsonHelper.getAsBoolean(properties, "showSummaryOnDebugScreen", true);
			this.memoryUsageWarningPercentage = GsonHelper.getAsInt(properties, "memoryUsageWarningPercentage", 90);
			this.debugChunkLoading = GsonHelper.getAsBoolean(properties, "debugChunkLoading", false);
			this.debugThreadsStuck = GsonHelper.getAsBoolean(properties, "debugThreadsStuck", false);
			this.disableEternalStarlightProgress = GsonHelper.getAsBoolean(properties, "disableEternalStarlightProgress", false);
			this.skipTickingUnloadedFluxNetworks = GsonHelper.getAsBoolean(properties, "skipTickingUnloadedFluxNetworks", false);
		} catch (Throwable e) {
			AllTheLeaks.LOGGER.error("Failed to load config file", e);
		} finally {
			save();
		}
	}

}
