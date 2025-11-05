package dev.uncandango.alltheleaks.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.config.ATLProperties;
import dev.uncandango.alltheleaks.diag.common.mods.minecraft.DebugChunkLoading;
import dev.uncandango.alltheleaks.diag.common.mods.minecraft.DebugNativeImage;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.MemoryMonitor;
import dev.uncandango.alltheleaks.mixin.Trackable;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.embeddedt.modernfix.world.ThreadDumper;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class ATLCommands {

	public static void registerClientCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
		dispatcher.register(
			Commands.literal("atl")
				.then(
					Commands.literal("run_explicit_gc")
						.executes(cmd -> runGc(cmd.getSource()))
				)
				.then(Commands.literal("clear_native_images")
					.executes(cmd -> clearNativeImages(cmd.getSource()))
				)
				.then(
					Commands.literal("force_refresh")
						.executes(cmd -> checkLeaking(cmd.getSource(), true))
				)
				.then(
					Commands.literal("reset_statistics")
						.executes(cmd -> resetStatistics(cmd.getSource()))
				)
				.then(
					Commands.literal("thread_dump").requires(source -> ModList.get().isLoaded("modernfix"))
						.executes(cmd -> doModernFixThreadDump(cmd.getSource()))
				)
		);
	}

	private static int doModernFixThreadDump(CommandSourceStack source) {
		AllTheLeaks.LOGGER.error(ThreadDumper.obtainThreadDump());
		source.sendSystemMessage(Component.literal("Thread dump done, check latest.log"));
		return Command.SINGLE_SUCCESS;
	}

	private static int resetStatistics(CommandSourceStack source) {
		runGc(source);
		MemoryMonitor.Statistics.reset();
		return Command.SINGLE_SUCCESS;
	}

	public static void registerCommonCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
		dispatcher.register(
			Commands.literal("atl")
				.then(
					Commands.literal("track_chunk").requires(source -> ATLProperties.get().debugChunkLoading)
						.executes(cmd -> clearTrackingChunks(cmd.getSource()))
						.then(
							Commands.argument("x", IntegerArgumentType.integer())
								.then(
									Commands.argument("z", IntegerArgumentType.integer())
										.executes(cmd -> startTrackingChunks(cmd.getSource(), IntegerArgumentType.getInteger(cmd, "x"), IntegerArgumentType.getInteger(cmd, "z"), null))
										.then(
											Commands.argument("dimension", DimensionArgument.dimension())
												.executes(cmd -> startTrackingChunks(cmd.getSource(), IntegerArgumentType.getInteger(cmd, "x"), IntegerArgumentType.getInteger(cmd, "z"), DimensionArgument.getDimension(cmd, "dimension")))
										)
								)
						)
				)
		);
	}

	public static void registerServerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
		dispatcher.register(
			Commands.literal("atl")
				.then(
					Commands.literal("run_explicit_gc")
						.executes(cmd -> runGc(cmd.getSource()))
				)
				.then(
					Commands.literal("force_refresh")
						.executes(cmd -> checkLeaking(cmd.getSource(), true))
				)
				.then(
					Commands.literal("reset_statistics")
						.executes(cmd -> resetStatistics(cmd.getSource()))
				)
				.then(
					Commands.literal("thread_dump").requires(source -> ModList.get().isLoaded("modernfix"))
						.executes(cmd -> doModernFixThreadDump(cmd.getSource()))
				)
		);
	}

	public static int startTrackingChunks(CommandSourceStack source, int x, int z, @Nullable ServerLevel level) {
		if (level == null) {
			level = source.getLevel();
		}
		if (level == null) {
			source.sendFailure(Component.literal("Missing dimension argument!"));
			return 0;
		}
		var chunkPos = new ChunkPos(x, z);
		var dimension = level.dimension();
		DebugChunkLoading.addTrackingChunk(dimension, chunkPos);
		source.sendSuccess(() -> Component.translatable("Starting to track events for chunk %s from dimension %s", Component.literal(chunkPos.toString()).withStyle(ChatFormatting.GREEN), Component.literal(dimension.location().toString()).withStyle(ChatFormatting.YELLOW)), true);
		return 1;
	}

	public static int clearTrackingChunks(CommandSourceStack source) {
		DebugChunkLoading.clearTrackingChunks();
		source.sendSuccess(() -> Component.literal("Cleared tracking chunks, now tracking everything."), true);
		return 1;
	}

	public static int checkLeaking(CommandSourceStack source, boolean shouldRunGc) {
		if (shouldRunGc) {
			if (runGc(source) == 0) {
				return 0;
			}
		}
		Trackable.clearNullReferences();

		// Events for logging

		AllTheLeaks.LOGGER.info("Logging events from checking leak");
		MemoryMonitor.getEventsSummary().forEach(AllTheLeaks.LOGGER::info);

		List<Component> lines = new ArrayList<>();
		Trackable.getSummary().forEach((baseClazz, summaryMap) -> {
			if (summaryMap.isEmpty()) {
				return;
			}
			lines.add(Component.translatable("%s:", baseClazz.getSimpleName()));
			summaryMap.forEach((innerClazz, count) -> {
				var module = innerClazz.getModule();
				if (module != null) {
					lines.add(Component.translatable("- %s (%s): %s", innerClazz.getSimpleName(), module.getName(), count));
				} else {
					lines.add(Component.translatable("- %s: %s", innerClazz.getSimpleName(), count));
				}
			});
		});
		if (lines.isEmpty()) {
			source.sendSystemMessage(Component.literal("No leak was found so far...").withStyle(ChatFormatting.GREEN));
		} else {
			source.sendSystemMessage(Component.literal("Listing leaks...").withStyle(ChatFormatting.YELLOW));
			lines.forEach(source::sendSystemMessage);
		}
		return Command.SINGLE_SUCCESS;
	}

	private static int clearNativeImages(CommandSourceStack source) {
		if (!ATLProperties.get().debugNativeImage) {
			source.sendFailure(Component.literal("DebugNativeImage is disabled, activate it at config/alltheleaks.json"));
			return 0;
		}
		AtomicInteger counter = new AtomicInteger();
		DebugNativeImage.NATIVE_IMAGES_TRACKER.forEach((k, v) -> {
			var setWithEmptyRef = Collections.synchronizedSet(new HashSet<DebugNativeImage.Value>());
			v.forEach(wr -> {
				var ref = wr.imageRef().get();
				if (ref == null) {
					setWithEmptyRef.add(wr);
				}
			});
			if (!v.isEmpty() && setWithEmptyRef.size() == v.size()) {
				AllTheLeaks.LOGGER.info("Removed all NativeImages for key {}", k);
				if (k.useStbFree()) {
					STBImage.nstbi_image_free(k.pixels());
				} else {
					MemoryUtil.nmemFree(k.pixels());
				}
				setWithEmptyRef.forEach(wr -> {
					counter.getAndIncrement();
					AllTheLeaks.LOGGER.info("Printing stack trace for: {}", wr.description());
					var reachedInit = false;
					for (StackTraceElement trace : wr.stackTraceElements()) {
						if (!reachedInit) {
							reachedInit = trace.getMethodName().contains("init");
						}
						if (reachedInit) {
							System.out.println("\tat " + trace.getClassName() + "." + trace.getMethodName() +
								"(" + trace.getFileName() + ":" + trace.getLineNumber() + ")");
						}

					}
				});
				v.clear();
			}
		});
		source.sendSuccess(() -> Component.literal("Cleared " + counter.get() + " NativeImages"), true);
		return 1;
	}

	public static int runGc(CommandSourceStack source) {
		var server = ServerLifecycleHooks.getCurrentServer();
		if (server != null) {
			server.executeBlocking(() -> server.saveEverything(true, true, true));
		}
		if (!MemoryMonitor.runExplicitGc()) {
			source.sendFailure(Component.literal("Explicit GC is disabled, remove arguments -XX:+DisableExplicitGC"));
			return 0;
		}
		return Command.SINGLE_SUCCESS;
	}
}