package dev.uncandango.alltheleaks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
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
	private static final JsonElement defaultProperties = getDefaultProperties();
	private static ATLProperties INSTANCE;
	public boolean ingredientDedupe;
	public boolean entitySectionCME;
	public boolean scoreboardDebug;
	public boolean debugItemStackModifications;
	public int version;
	private JsonObject properties;

	private ATLProperties() {
		load();
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
			this.scoreboardDebug = GsonHelper.getAsBoolean(properties, "scoreboardDebug", false);
			this.entitySectionCME = GsonHelper.getAsBoolean(properties, "entitySectionCME", false);
			this.debugItemStackModifications = GsonHelper.getAsBoolean(properties, "debugItemStackModifications", false);
		} catch (IOException e) {
			AllTheLeaks.LOGGER.error("Failed to load config file", e);
			properties = new JsonObject(); // Initialize with an empty JsonObject in case of error
		}
	}

	public static void save() {
		try (var writer = Files.newBufferedWriter(path)) {
			GSON.toJson(defaultProperties, writer);
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

	private static JsonElement getDefaultProperties() {
		var properties = new JsonObject();
		properties.addProperty("ingredientDedupe", false);
		properties.addProperty("entitySectionCME", false);
		properties.addProperty("scoreboardDebug", false);
		properties.addProperty("debugItemStackModifications", false);
		properties.addProperty("version", 1);
		return properties;
	}

}
