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
	private JsonObject properties;
	private static final JsonElement defaultProperties = getDefaultProperties();
	private static ATLProperties INSTANCE;


	public boolean preventSearchIgnoredItems;
	public boolean ingredientDedupe;
	public boolean resourceLocationDedupe;

	private ATLProperties() {
		load();
	}

	public static ATLProperties get() {
		if(INSTANCE == null) {
			INSTANCE = new ATLProperties();
		}
		return INSTANCE;
	}

	private static JsonElement getDefaultProperties() {
		var properties = new JsonObject();
		properties.addProperty("preventSearchIgnoredItems", false);
		properties.addProperty("ingredientDedupe", true);
		properties.addProperty("resourceLocationDedupe", false);
		return properties;
	}

	public void load() {
		if (!path.toFile().exists()) {
			save();
		}
		try (var reader = Files.newBufferedReader(path)) {
			properties = GSON.fromJson(reader, JsonObject.class);
			this.preventSearchIgnoredItems = GsonHelper.getAsBoolean(properties, "preventSearchIgnoredItems", false);
			this.ingredientDedupe = GsonHelper.getAsBoolean(properties, "ingredientDedupe", true);
			this.resourceLocationDedupe = GsonHelper.getAsBoolean(properties, "resourceLocationDedupe", false);
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

}
