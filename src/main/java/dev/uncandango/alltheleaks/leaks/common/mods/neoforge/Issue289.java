package dev.uncandango.alltheleaks.leaks.common.mods.neoforge;

import com.mojang.datafixers.util.Pair;
import cpw.mods.jarhandling.impl.Jar;
import cpw.mods.jarhandling.impl.JarContentsImpl;
import cpw.mods.niofs.union.UnionFileSystem;
import cpw.mods.niofs.union.UnionFileSystemProvider;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.system.MemoryUtil;
import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.nio.file.FileSystem;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static dev.uncandango.alltheleaks.AllTheLeaks.LOGGER;

@Issue(issueId = "#289", modId = "neoforge", versionRange = "[21.,21.1.182]", description = "Memory leak on FancyModLoader https://github.com/neoforged/FancyModLoader/issues/289")
public class Issue289 {
	public Issue289() {
		var modEventBus = ModList.get().getModContainerById(AllTheLeaks.MOD_ID).get().getEventBus();
		modEventBus.addListener(this::closeDanglingJars);
	}

	@SuppressWarnings("deprecation")
	private void closeDanglingJars(FMLLoadCompleteEvent event){
		event.enqueueWork(() -> {
			Map<String, UnionFileSystem> copyFileSystems = new HashMap<>();
			Set<FileSystem> validFS = new HashSet<>();
			try {
				Unsafe UNSAFE = ObfuscationReflectionHelper.getPrivateValue(MemoryUtil.class, null, "UNSAFE");
				if (UNSAFE == null) throw new IllegalAccessException("Not possible to grab UNSAFE");
				Field fieldImplLookup = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
				var LOOKUP = (MethodHandles.Lookup) UNSAFE.getObject(UNSAFE.staticFieldBase(fieldImplLookup), UNSAFE.staticFieldOffset(fieldImplLookup));
				var UFSP = (UnionFileSystemProvider) LOOKUP.findStaticVarHandle(JarContentsImpl.class, "UFSP", UnionFileSystemProvider.class).get();
				var fileSystems = (Map<String, UnionFileSystem>) LOOKUP.findVarHandle(UnionFileSystemProvider.class, "fileSystems", Map.class).get(UFSP);
				copyFileSystems.putAll(fileSystems);
				var fsVH = LOOKUP.findVarHandle(Jar.class, "filesystem", UnionFileSystem.class);
				ModList.get().getModFiles().stream().map(modFile -> (UnionFileSystem)fsVH.get(modFile.getFile().getSecureJar())).forEach(validFS::add);
			} catch (Throwable e) {
				LOGGER.error("Failed to clear dangling mod jars", e);
			}

			Pattern MATCH_PATTERN = Pattern.compile("(.*)#(\\d+)");
			Map<String, Pair<Integer,UnionFileSystem>> tempMap = new HashMap<>();
			for (Map.Entry<String, UnionFileSystem> entry : copyFileSystems.entrySet()) {
				var key = entry.getKey();
				if (StringUtils.countMatches(key, '#') > 1) continue; // Ignore Jarjars for now
				var value = entry.getValue();
				var match = MATCH_PATTERN.matcher(key);
				if (match.find()) {
					var newkey = match.group(1);
					var newIndex = Integer.valueOf(match.group(2));
					var oldVal = tempMap.put(newkey, Pair.of(newIndex, value));
					if (oldVal != null) {
						if (oldVal.getFirst() < newIndex) {
							try {
								if (validFS.contains(oldVal.getSecond())) {
									LOGGER.warn("Tried to release a valid jar {}#{}, skipping...", newkey, oldVal.getFirst());
								} else {
									LOGGER.info("Releasing leaked jar: {}#{}", newkey, oldVal.getFirst());
									oldVal.getSecond().close();
								}
							} catch (IOException e) {
								LOGGER.error("Failed to close leaked jar.", e);
							}
						} else {
							try {
								if (validFS.contains(value)) {
									LOGGER.warn("Tried to release a valid jar {}#{}, skipping...", newkey, newIndex);
								} else {
									LOGGER.info("Releasing leaked jar: {}#{}", newkey, newIndex);
									value.close();
								}
							} catch (IOException e) {
								LOGGER.error("Failed to close leaked jar.", e);
							}
						}
					}
				}
			}
		});
	}
}
