package dev.uncandango.alltheleaks.leaks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.AnnotationHelper;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.config.ATLProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.VersionRange;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class IssueManager {
	private static final List<String> constructors = new ArrayList<>();
	private static final List<ModFileScanData.AnnotationData> annotations = new ArrayList<>();
	private static Set<String> mixinAllowed;
	private static Set<String> mixinToCancel;

	public static Set<String> getMixinToCancel() {
		if (mixinToCancel == null) {
			getAllowedMixins();
		}
		return mixinToCancel;
	}

	public static Set<String> getAllowedMixins() {
		if (mixinAllowed == null) {
			mixinAllowed = Sets.newHashSet();
			mixinToCancel = Sets.newHashSet();
			var scanData = LoadingModList.get().getModFileById(AllTheLeaks.MOD_ID).getFile().getScanResult();
			var currentDist = FMLEnvironment.dist;
			scanData.getAnnotations().stream()
				.filter(ad -> ad.annotationType().equals(Type.getType(Issue.class)))
				.forEach(annotation -> {
					if (AllTheLeaks.INDEV) {
						annotations.add(annotation);
					}
					String issueId = AnnotationHelper.getValue(annotation, "issueId");
					String modId = AnnotationHelper.getValue(annotation, "modId");
					List<String> extraModDep = AnnotationHelper.getValue(annotation, "extraModDep");
					List<String> extraModDepVersions = AnnotationHelper.getValue(annotation, "extraModDepVersions");
					String flag = AnnotationHelper.getValue(annotation, "config");
					String modAbsent = AnnotationHelper.getValue(annotation, "onlyIfModAbsent");
					boolean flagActivated;
					if (Objects.equals(flag, "")) {
						flagActivated = true;
					} else {
						try {
							Field configValue = ATLProperties.class.getField(flag);
							flagActivated = (boolean) configValue.get(ATLProperties.get());
						} catch (NoSuchFieldException | IllegalAccessException e) {
							AllTheLeaks.LOGGER.warn("Config property {} does not exist!", flag);
							return;
						}
					}
					if (!flagActivated) {
						AllTheLeaks.LOGGER.info("Skipping feature {} from mod {} as it's feature flag is not activated!", issueId, modId);
						return;
					}
					if (modAbsent != null && LoadingModList.get().getModFileById(modAbsent) != null) {
						AllTheLeaks.LOGGER.info("Skipping issue {} from mod {} as mod {} is present!", issueId, modId, modAbsent);
						return;
					}
					Boolean devOnly = AnnotationHelper.getValue(annotation, "devOnly");
					if (devOnly && !AllTheLeaks.INDEV) {
						AllTheLeaks.LOGGER.info("Skipping issue {} from mod {} as it's dev only!", issueId, modId);
						return;
					}
					if (currentDist.isDedicatedServer()) {
						Dist side = annotation.memberName().contains(".client.mods.") ? Dist.CLIENT : Dist.DEDICATED_SERVER;
						if (side.isClient()) {
							AllTheLeaks.LOGGER.info("Skipping issue {} from mod {} as it is client side only!", issueId, modId);
							return;
						}
					}
					String versionRange = AnnotationHelper.getValue(annotation, "versionRange");
					List<String> mixins = AnnotationHelper.getValue(annotation, "mixins");
					List<String> mixinsToCancel = AnnotationHelper.getValue(annotation, "mixinsToCancel");
					var condition = generateCondition(modId, versionRange, annotation.memberName(), extraModDep, extraModDepVersions);
					if (condition.get()) {
						if (mixins != null && !mixins.isEmpty()) {
							AllTheLeaks.LOGGER.info("Mixins added to allowed list: {}", mixins);
							mixinAllowed.addAll(mixins);
						}
						if (mixinsToCancel != null && !mixinsToCancel.isEmpty()) {
							AllTheLeaks.LOGGER.info("Mixins added to cancel list: {}", mixinsToCancel);
							mixinToCancel.addAll(mixinsToCancel);
						}
					}
				});
		}
		return mixinAllowed;
	}

	public static Supplier<Boolean> generateCondition(String modId, String versionRange, String annotatedClass, List<String> extraModDep, List<String> extraModDepVersions) {
		return () -> {
			var mod = LoadingModList.get().getModFileById(modId);
			if (mod != null) {
				try {
					var range = VersionRange.createFromVersionSpec(versionRange);
					var modVerString = mod.versionString();
					var modVer = new DefaultArtifactVersion(modVerString);
					if (range.containsVersion(modVer) || modVerString.equals("0.0NONE")) {
						if (!extraModDep.isEmpty()) {
							for (int i = 0; i < extraModDep.size(); i++) {
								var dep = LoadingModList.get().getModFileById(extraModDep.get(i));
								if (dep == null) {
									AllTheLeaks.LOGGER.info("Extra dependency Mod {} is not present in the mod list", extraModDep.get(i));
									AllTheLeaks.LOGGER.info("Class {} will NOT be loaded as extra mod dependecy is not present.", annotatedClass);
									return false;
								}
								var rangeDepVer = VersionRange.createFromVersionSpec(extraModDepVersions.get(i));
								var depVerString = dep.versionString();
								var depVer = new DefaultArtifactVersion(depVerString);
								if (rangeDepVer.containsVersion(depVer) || depVerString.equals("0.0NONE")) {
									AllTheLeaks.LOGGER.info("Extra dependecy Mod {} matches versions: {} in {}", extraModDep.get(i), dep.versionString(), extraModDepVersions.get(i));
								} else {
									AllTheLeaks.LOGGER.info("Extra dependecy Mod {} does NOT matches versions: {} in {}", extraModDep.get(i), dep.versionString(), extraModDepVersions.get(i));
									AllTheLeaks.LOGGER.info("Class {} will NOT be loaded as extra mod dependecy does not match.", annotatedClass);
									return false;
								}
							}
						}
						constructors.add(annotatedClass);
						AllTheLeaks.LOGGER.info("Class {} will be loaded as it matches versions: {} in {}", annotatedClass, modVer, range);
						return true;
					} else {
						AllTheLeaks.LOGGER.info("Class {} will NOT be loaded as mod {} does not match versions: {} in {}", annotatedClass, modId, modVer, range);
					}
				} catch (Exception e) {
					AllTheLeaks.LOGGER.error("Error while comparing versions and instantiating class", e);
				}
			} else {
				AllTheLeaks.LOGGER.info("Class {} will NOT be loaded as mod {} is not present", annotatedClass, modId);
			}
			return false;
		};
	}

	public static void initiateIssues() {
		constructors.forEach(ctor -> {
			try {
				var clazz = Class.forName(ctor);
				Method instanceMethod = null;
				try {
					instanceMethod = clazz.getDeclaredMethod("getInstance");
				} catch (NoSuchMethodException ignore) {
				}
				if (instanceMethod != null) {
					instanceMethod.invoke(null);
				} else {
					var ctorClazz = clazz.getDeclaredConstructors()[0];
					ctorClazz.newInstance();
				}
			} catch (Throwable e) {
				AllTheLeaks.LOGGER.error("Failed to instantiate constructor.", e);
			}
		});
	}

	public static void generateIssueSummary() {
		var categoryModMap = new HashMap<String, Map<String, Map<String, List<String>>>>();
		var modListOpenEnded = new HashSet<String>();
		annotations.forEach(ad -> {
			var category = ad.memberName().split("\\.")[3];
			categoryModMap.putIfAbsent(category, Maps.newHashMap());
			String modId = AnnotationHelper.getValue(ad, "modId");
			categoryModMap.get(category).putIfAbsent(modId, Maps.newHashMap());
			String versionRange = AnnotationHelper.getValue(ad, "versionRange");
			if (versionRange.endsWith(",)") && category.equals("leaks") && !modId.equals("minecraft")) {
//				too long
//				var info = ModList.get().getModFileById(modId);
//				var author = info != null ? info.getConfig().getConfigElement("authors").orElse(null) : null;
//				if (author == null) {
//					author = info != null ? info.getConfig().getConfigList("mods").get(0).getConfigElement("authors").orElse(null) : null;
//				}
//				modListOpenEnded.add(modId + (author != null ? " by " + author : ""));
				modListOpenEnded.add(modId);
			}
			categoryModMap.get(category).get(modId).putIfAbsent(versionRange, Lists.newArrayList());
			String flag = AnnotationHelper.getValue(ad, "config");
			String description = AnnotationHelper.getValue(ad, "description");
			boolean flagDefault = AnnotationHelper.getValue(ad, "configActivated");
			var featureString = flag.isEmpty() ? "" : " - (default: " + (flagDefault ? "**ON**" : "**OFF**") + ")";
			categoryModMap.get(category).get(modId).get(versionRange).add(String.format("%s%s", description, featureString));
		});

		var sb = new StringBuilder();
		categoryModMap.forEach((category, modMap) -> {
			var categoryName = StringUtils.capitalize(category);
			if (categoryName.equals("Metrics")) {
				categoryName = "Metrics (Dev Only `-Dalltheleaks.indev=true`)";
			}
			if (categoryName.equals("Feature")) {
				categoryName = "Feature (`config/alltheleaks.json`)";
			}
			sb.append("## ").append(StringUtils.capitalize(categoryName)).append("\n\n");
			modMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach((entry) -> {
				sb.append("- ").append(entry.getKey()).append("\n");
				entry.getValue().forEach((versionRange, strings) -> {
					sb.append("  - ").append(versionRange).append("\n");
					strings.forEach(s -> sb.append("    - ").append(s).append("\n"));
				});
			});
		});

		AllTheLeaks.LOGGER.info("Mod list open ended: {}", modListOpenEnded);

		try (var writer = Files.newBufferedWriter(FMLPaths.GAMEDIR.get().resolve("issues.md"), StandardCharsets.UTF_8)) {
			writer.write(sb.toString());
		} catch (IOException e) {
			AllTheLeaks.LOGGER.error("Failed to write issues.md", e);
		}
	}
}
