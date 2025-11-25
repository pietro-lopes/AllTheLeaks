package dev.uncandango.alltheleaks.feature.common.mods.minecraft;

import com.mojang.brigadier.Command;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.FileUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.storage.FileNameDateFormatter;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.neoforged.neoforge.common.world.chunk.TicketController;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Issue(modId = "minecraft", issueId = "Backup save with loaded chunks only" ,versionRange = "1.21.1")
public class SaveWithLoadedChunks {
	private static final MethodHandle GET_CHUNKS = ReflectionHelper.getMethodFromClass(ChunkMap.class, "getChunks", MethodType.methodType(Iterable.class), false);
	private static final MethodHandle CHECK_LOCK = ReflectionHelper.getMethodFromClass(LevelStorageSource.LevelStorageAccess.class, "checkLock", MethodType.methodType(void.class), false);
	private static final VarHandle STORAGE_SOURCE = ReflectionHelper.getFieldFromClass(MinecraftServer.class, "storageSource", LevelStorageSource.LevelStorageAccess.class, false);
	private static final DateTimeFormatter FORMATTER = FileNameDateFormatter.create();
	private static final UUID CHUNK_OWNER = UUID.fromString("fe2efb68-0207-4255-b2dd-02d197309304");
	public static final TicketController ATL_CHUNKLOADER = new TicketController(ResourceLocation.fromNamespaceAndPath(AllTheLeaks.MOD_ID,"chunkloader"), (level,helper) -> helper.removeAllTickets(CHUNK_OWNER));


	public static int loadChunksFromSaveFile(CommandSourceStack source){
		var server = source.getServer();
		LevelStorageSource.LevelStorageAccess storage = (LevelStorageSource.LevelStorageAccess) STORAGE_SOURCE.get(server);
		var worldDir = storage.getWorldDir();
		var worldName = storage.getLevelId();

		var chunksFile = worldDir.resolve(worldName).resolve("alltheleaks_chunks.dat");

		var count = new AtomicInteger();
		if (Files.exists(chunksFile)) {
			try {
				var loadedChunksData = NbtIo.readCompressed(chunksFile, NbtAccounter.unlimitedHeap());
				for (var dimension : loadedChunksData.getAllKeys()) {
					var level = server.getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(dimension)));
					if (level == null) {
						AllTheLeaks.LOGGER.warn("Level {} was not found while trying to load chunks", dimension);
					} else {
						var listTickets = loadedChunksData.get(dimension);
						if (listTickets instanceof ListTag lt) {
							for (int i = 30; i <= 33; i++) {
								for (var chunk : lt.getLongArray(i)) {
									var chunkPos = new ChunkPos(chunk);
									if (i <= 31) {
										ATL_CHUNKLOADER.forceChunk(level, CHUNK_OWNER, chunkPos.x, chunkPos.z, true, true);
										count.incrementAndGet();
										AllTheLeaks.LOGGER.debug("Chunkloaded {} from dimension {} is with ticket {}", chunkPos, dimension, i);
									}
									if (i == 33) {
										level.getBlockState(chunkPos.getMiddleBlockPosition(60));
									}
								}
							}
						}
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			source.sendFailure(Component.literal("No AllTheLeaks file with chunk info was found on this save."));
			return 0;
		}
		source.sendSuccess(() -> Component.translatable("Forceloaded %s chunks from backup. It will be unloaded when world is loaded again.", count.get()),false);
		return Command.SINGLE_SUCCESS;
	}

	public static int saveWorld(CommandSourceStack source){
		var server = source.getServer();
		LevelStorageSource.LevelStorageAccess storage = (LevelStorageSource.LevelStorageAccess) STORAGE_SOURCE.get(server);
        var worldDir = storage.getWorldDir();
		var worldName = storage.getLevelId();

		String fileName = LocalDateTime.now().format(FORMATTER) + "_" + worldName;

		try {
			FileUtil.createDirectoriesSafe(AllTheLeaks.getLocal());
		} catch (IOException ioexception) {
			throw new RuntimeException(ioexception);
		}

		try {
			CHECK_LOCK.invoke(storage);
		} catch (Throwable e) {
			AllTheLeaks.LOGGER.error("Error while invoking checkLock", e);
			return 0;
		}
		server.executeBlocking(() -> server.saveEverything(true, true, true));

		var ticketPerLevel = new CompoundTag();

		Map<String, Set<String>> regionsToSave = new HashMap<>();
		for (var level : server.getAllLevels()) {
			try {
				for (var chunkholder : (Iterable<ChunkHolder>)GET_CHUNKS.invoke(level.getChunkSource().chunkMap)) {
					var chunk = chunkholder.getLatestChunk();
					if (chunk == null) continue;
					var dimension = level.dimension().location().toString();
					var chunkPos = chunk.getPos();
					if (!ticketPerLevel.contains(dimension)) {
						var taglist = new ListTag(47);
						for (var i = 0; i < 47; i++) {
							taglist.add(i, new LongArrayTag(new long[0]));
						}
						ticketPerLevel.put(dimension, taglist);
					}
					((LongArrayTag) ticketPerLevel.getList(dimension, Tag.TAG_LONG_ARRAY).get(chunkholder.getTicketLevel())).add(LongTag.valueOf(chunkPos.toLong()));
					regionsToSave.computeIfAbsent(dimension, key -> new HashSet<>()).add(String.format(Locale.ROOT, "r.%d.%d", chunkPos.getRegionX(), chunkPos.getRegionZ()));
				}
			} catch (Throwable e) {
				AllTheLeaks.LOGGER.error("Error while invoking getChunks", e);
				return 0;
			}
		}

		var outputFile = getOutputFile(fileName);

		try {
			try (ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(outputFile), 64 * 1024))) {
				Path relative = Paths.get(worldName);
				Path savePath = worldDir.resolve(relative).toRealPath();

				String s1 = relative.resolve("alltheleaks_chunks.dat").toString().replace('\\', '/');
				ZipEntry zipentry = new ZipEntry(s1);
				zipStream.putNextEntry(zipentry);
				var byteStream = new ByteArrayOutputStream();
				NbtIo.writeCompressed(ticketPerLevel, byteStream);
				byteStream.writeTo(zipStream);
				zipStream.closeEntry();

				Files.walkFileTree(savePath, new SimpleFileVisitor<Path>() {
					private static final Set<String> validFolders = Set.of("entities","poi","region");

					@NotNull
					@Override
					public FileVisitResult visitFile(@NotNull Path file, @NotNull BasicFileAttributes attrs) throws IOException {
						if (file.endsWith("session.lock")) {
							return FileVisitResult.CONTINUE;
						} else {
							if (file.getFileName().toString().startsWith("r.")) {
								String dimension = null;
								var parent1 = file.getParent();
								if (validFolders.contains(parent1.getFileName().toString())) {
									//AllTheLeaks.LOGGER.info("Region file at {}", file);
									var parent2 = parent1.getParent();
									if (parent2.equals(savePath)) {
										dimension = "minecraft:overworld";
									} else {
										var p2 = parent2.getFileName().toString();
										if (p2.equals("DIM1")) {
											dimension = "minecraft:the_end";
										} else if (p2.equals("DIM-1")) {
											dimension = "minecraft:the_nether";
										}
										if (dimension == null) {
											var p3 = parent2.getParent();
											dimension = p3.getFileName().toString() + ":" + p2;
										}
									}
									var fullRegionFileName = file.getFileName().toString();
									var regionFileName = fullRegionFileName.substring(0,fullRegionFileName.lastIndexOf('.'));
									var validRegions = regionsToSave.get(dimension);
									if (validRegions == null || !validRegions.contains(regionFileName)) {
										return FileVisitResult.CONTINUE;
									}
								}
							}
							String s1 = relative.resolve(savePath.relativize(file)).toString().replace('\\', '/');
							ZipEntry zipentry = new ZipEntry(s1);
							zipStream.putNextEntry(zipentry);
							com.google.common.io.Files.asByteSource(file.toFile()).copyTo(zipStream);
							zipStream.closeEntry();
							return FileVisitResult.CONTINUE;
						}
					}
				});
			}
		} catch (IOException e) {
			AllTheLeaks.LOGGER.error("Error while trying to save world", e);
			throw new RuntimeException(e);
		}

		source.sendSuccess(() -> Component.literal("Backup with only loaded chunks saved at ./local/alltheleaks/"), true);
		return Command.SINGLE_SUCCESS;
	}

	private static Path getOutputFile(String name){
		try {
			Files.createDirectories(AllTheLeaks.getLocal());
			return AllTheLeaks.getLocal().resolve(FileUtil.findAvailableName(AllTheLeaks.getLocal(), name, ".zip"));
		} catch (IOException e) {
			AllTheLeaks.LOGGER.error("Error while creating folder", e);
			throw new RuntimeException(e);
		}
	}
}
