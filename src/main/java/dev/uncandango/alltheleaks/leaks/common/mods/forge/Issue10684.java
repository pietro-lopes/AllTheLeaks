package dev.uncandango.alltheleaks.leaks.common.mods.forge;

import com.mojang.datafixers.util.Pair;
import cpw.mods.cl.JarModuleFinder;
import cpw.mods.cl.ModuleClassLoader;
import cpw.mods.jarhandling.SecureJar;
import cpw.mods.jarhandling.impl.Jar;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.ModuleLayerHandler;
import cpw.mods.modlauncher.api.IModuleLayerManager;
import cpw.mods.niofs.union.UnionFileSystem;
import cpw.mods.niofs.union.UnionFileSystemProvider;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.system.MemoryUtil;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.nio.file.ClosedFileSystemException;
import java.nio.file.FileSystem;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Issue(modId = "forge", issueId = "#10684", versionRange = "*", description = "Memory leak on FML") // , config = "clearJars"
public class Issue10684 {
	@SuppressWarnings("removal")
	public Issue10684() {
		var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::closeDanglingJars);
	}

	private void closeDanglingJars(FMLLoadCompleteEvent event){
		event.enqueueWork(() -> {
			Map<String, UnionFileSystem> copyFileSystems = new HashMap<>();
			Set<FileSystem> validFS = new HashSet<>();
			try {
				Unsafe UNSAFE;
				if (FMLEnvironment.dist.isClient()) {
					UNSAFE = ObfuscationReflectionHelper.getPrivateValue(MemoryUtil.class, null, "UNSAFE");
				} else {
					UNSAFE = ObfuscationReflectionHelper.getPrivateValue(Unsafe.class, null, "theUnsafe");
				}

				if (UNSAFE == null) throw new IllegalAccessException("Not possible to grab UNSAFE");
				Field fieldImplLookup = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");

				MethodHandles.Lookup LOOKUP = (MethodHandles.Lookup) UNSAFE.getObject(UNSAFE.staticFieldBase(fieldImplLookup), UNSAFE.staticFieldOffset(fieldImplLookup));

				VarHandle fsVH = LOOKUP.findVarHandle(Jar.class, "filesystem", UnionFileSystem.class);
				VarHandle layersVH = LOOKUP.findVarHandle(ModuleLayerHandler.class, "completedLayers", EnumMap.class);
				Class<?> layerInfo = Arrays.stream(ModuleLayerHandler.class.getNestMembers()).filter(clazz -> clazz.getName().contains("LayerInfo")).findFirst().orElseThrow();
				VarHandle clVH = LOOKUP.findVarHandle(layerInfo, "cl", ModuleClassLoader.class);
				VarHandle rootsVH = LOOKUP.findVarHandle(ModuleClassLoader.class, "resolvedRoots", Map.class);
				Class<?> jmr = Arrays.stream(JarModuleFinder.class.getNestMembers()).filter(clazz -> clazz.getName().contains("JarModuleReference")).findFirst().orElseThrow();
				VarHandle jarVH = LOOKUP.findVarHandle(jmr, "jar", SecureJar.ModuleDataProvider.class);
				Class<?> jmdp = Arrays.stream(Jar.class.getNestMembers()).filter(clazz -> clazz.getName().contains("JarModuleDataProvider")).findFirst().orElseThrow();
				VarHandle jar2VH = LOOKUP.findVarHandle(jmdp, "jar", Jar.class);
				VarHandle ufspVH = LOOKUP.findStaticVarHandle(Jar.class, "UFSP", UnionFileSystemProvider.class);

				UnionFileSystemProvider UFSP = (UnionFileSystemProvider) ufspVH.get();

				Map<String, UnionFileSystem> fileSystems = (Map<String, UnionFileSystem>) LOOKUP.findVarHandle(UnionFileSystemProvider.class, "fileSystems", Map.class).get(UFSP);
				copyFileSystems.putAll(fileSystems);

				ModList.get().getModFiles().stream().map(modFile -> (UnionFileSystem)fsVH.get(modFile.getFile().getSecureJar())).forEach(validFS::add);

				IModuleLayerManager layerManager = Launcher.INSTANCE.environment().findModuleLayerManager().orElseThrow();
				EnumMap<IModuleLayerManager.Layer, ?> map = (EnumMap)layersVH.get(layerManager);

				for (var layer : IModuleLayerManager.Layer.values()) {
					Optional.ofNullable(map.get(layer)).ifPresent(info -> {
						ModuleClassLoader cl = (ModuleClassLoader)clVH.get(info);
						Map<String, ?> roots = (Map<String, ?>)rootsVH.get(cl);
						roots.values().forEach(reference -> {
							SecureJar.ModuleDataProvider provider = (SecureJar.ModuleDataProvider)jarVH.get(reference);
							if (jmdp.isAssignableFrom(provider.getClass())) {
								Jar jar = (Jar)jar2VH.get(provider);
								validFS.add((UnionFileSystem)fsVH.get(jar));
							} else {
								try {
									var field = provider.getClass().getDeclaredField("provider");
									field.setAccessible(true);
									var jmdpValue = field.get(provider);
									var jar = (Jar)jar2VH.get(jmdpValue);
									validFS.add((UnionFileSystem)fsVH.get(jar));
								} catch (NoSuchFieldException | IllegalAccessException ignore) {
								}
								//AllTheLeaks.LOGGER.debug("Provider is not JarModuleDataProvider: {}", provider);
							}
						});
					});
				}
			} catch (Throwable e) {
				AllTheLeaks.LOGGER.error("Failed to clear dangling mod jars", e);
				return;
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
									AllTheLeaks.LOGGER.debug("Tried to release a valid jar {}#{}, skipping...", newkey, oldVal.getFirst());
									AllTheLeaks.LOGGER.debug("Will try {}", newIndex);
									if (!validFS.contains(value)) {
										AllTheLeaks.LOGGER.debug("Releasing leaked jar: {}#{}", newkey, newIndex);
										value.close();
									}
								} else {
									AllTheLeaks.LOGGER.debug("Releasing leaked jar: {}#{}", newkey, oldVal.getFirst());
									oldVal.getSecond().close();
								}
							} catch (ClosedFileSystemException e) {
								AllTheLeaks.LOGGER.error("Failed to close leaked jar.", e);
							}
						} else {
							try {
								if (validFS.contains(value)) {
									AllTheLeaks.LOGGER.debug("Tried to release a valid jar {}#{}, skipping...", newkey, newIndex);
									AllTheLeaks.LOGGER.debug("Will try {}", oldVal.getFirst());
									if (!validFS.contains(oldVal.getSecond())) {
										AllTheLeaks.LOGGER.debug("Releasing leaked jar: {}#{}", newkey, oldVal.getFirst());
										oldVal.getSecond().close();
									}
								} else {
									AllTheLeaks.LOGGER.debug("Releasing leaked jar: {}#{}", newkey, newIndex);
									value.close();
								}
							} catch (ClosedFileSystemException e) {
								AllTheLeaks.LOGGER.error("Failed to close leaked jar.", e);
							}
						}
					}
				}
			}
		});
	}
}
