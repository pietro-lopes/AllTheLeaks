package dev.uncandango.alltheleaks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.uncandango.alltheleaks.AllTheLeaks;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.util.JsonUtils;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ATLProperties {
	private static final Gson GSON = new GsonBuilder().setLenient().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
	private static final Path path = FMLPaths.CONFIGDIR.get().resolve("alltheleaks.json");
	private static JsonObject properties;
	private static final JsonObject defaultProperties = getDefaultProperties();
	private static ATLProperties INSTANCE;

	public boolean preventSearchIgnoredItems;
	public boolean ingredientDedupe;
	public boolean resourceLocationDedupe;
	public boolean debugItemStackModifications;
	public int logIntervalInMinutes;
	public boolean showSummaryOnDebugScreen;
	public int memoryUsageWarningPercentage;

	private ATLProperties() {
		load();
	}

	public static ATLProperties get() {
		if(INSTANCE == null) {
			INSTANCE = new ATLProperties();
		}
		return INSTANCE;
	}

	private static JsonObject getDefaultProperties() {
		var properties = new JsonObject();
		properties.addProperty("preventSearchIgnoredItems", false);
		properties.addProperty("ingredientDedupe", false);
		properties.addProperty("resourceLocationDedupe", false);
		properties.addProperty("debugItemStackModifications", false);
		properties.addProperty("logIntervalInMinutes", 10);
		properties.addProperty("showSummaryOnDebugScreen", true);
		properties.addProperty("memoryUsageWarningPercentage", 90);
		return properties;
	}

	public void load() {
		if (!path.toFile().exists()) {
			save();
		}
		try (var reader = Files.newBufferedReader(path)) {
			properties = GSON.fromJson(reader, JsonObject.class);
			this.preventSearchIgnoredItems = GsonHelper.getAsBoolean(properties, "preventSearchIgnoredItems", false);
			this.ingredientDedupe = GsonHelper.getAsBoolean(properties, "ingredientDedupe", false);
			this.debugItemStackModifications = GsonHelper.getAsBoolean(properties, "debugItemStackModifications", false);
			this.resourceLocationDedupe = GsonHelper.getAsBoolean(properties, "resourceLocationDedupe", false);
			this.logIntervalInMinutes = GsonHelper.getAsInt(properties, "logIntervalInMinutes", 10);
			this.showSummaryOnDebugScreen = GsonHelper.getAsBoolean(properties, "showSummaryOnDebugScreen", true);
			this.memoryUsageWarningPercentage = GsonHelper.getAsInt(properties, "memoryUsageWarningPercentage", 90);
		} catch (IOException e) {
			AllTheLeaks.LOGGER.error("Failed to load config file", e);
		} finally {
			save();
		}
	}

	public static void save() {
		if (properties == null) {
			properties = defaultProperties.deepCopy();
		}
		try (var writer = Files.newBufferedWriter(path)) {
			defaultProperties.asMap().forEach((key,val) -> {
				properties.asMap().putIfAbsent(key, val);
			});
			GSON.toJson(properties, writer);
		} catch (IOException e) {
			AllTheLeaks.LOGGER.error("Failed to save config file", e);
		}
	}

}
