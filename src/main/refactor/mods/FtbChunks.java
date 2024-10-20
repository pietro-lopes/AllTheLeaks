package dev.uncandango.alltheleaks.mods;

import dev.ftb.mods.ftbchunks.client.FTBChunksClient;
import dev.uncandango.alltheleaks.AllTheLeaks;

import java.util.List;

public interface FtbChunks {
    static void run() {
        try {
            var mapIconsField = FTBChunksClient.INSTANCE.getClass().getDeclaredField("mapIcons");
            var inWorldMapIconsField = FTBChunksClient.INSTANCE.getClass().getDeclaredField("inWorldMapIcons");
            mapIconsField.setAccessible(true);
            ((List<?>) mapIconsField.get(FTBChunksClient.INSTANCE)).clear();
            inWorldMapIconsField.setAccessible(true);
            ((List<?>) inWorldMapIconsField.get(FTBChunksClient.INSTANCE)).clear();
        } catch (Exception e) {
            AllTheLeaks.LOGGER.warn("Failed to get map icons from FTB Chunks...");
        }
    }
}
