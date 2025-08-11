package dev.uncandango.alltheleaks.mixinsq;

import com.bawnorton.mixinsquared.adjuster.tools.AdjustableAnnotationNode;
import com.bawnorton.mixinsquared.adjuster.tools.AdjustableOverwriteNode;
import com.bawnorton.mixinsquared.adjuster.tools.type.MethodListAnnotationNode;
import com.bawnorton.mixinsquared.api.MixinAnnotationAdjuster;
import com.google.common.collect.Sets;
import cpw.mods.modlauncher.Launcher;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import dev.uncandango.alltheleaks.config.ATLProperties;
import dev.uncandango.alltheleaks.utils.MethodNodeHasher;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.loading.LoadingModList;
import org.embeddedt.modernfix.core.ModernFixMixinPlugin;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.service.MixinService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ATLMixinAdjuster implements MixinAnnotationAdjuster {
	private static final boolean IS_SRG = Launcher.INSTANCE.environment().findNameMapping("srg").isPresent();
	private static final AdjustableAnnotationNode UNIQUE = AdjustableAnnotationNode.fromNode(new AnnotationNode(Unique.class.descriptorString()));

	@Override
	public AdjustableAnnotationNode adjust(List<String> targetClassNames, String mixinClassName, MethodNode handlerNode, AdjustableAnnotationNode annotationNode) {
		if (!IS_SRG && mixinClassName.startsWith("dev.uncandango.alltheleaks.mixin.core.")) {
			return processHashes(targetClassNames,mixinClassName,annotationNode, handlerNode);
		}
		if (isModernfixFeatureOn("main.IngredientWithCountMixin", mixinClassName, "perf.faster_ingredients.IngredientMixin")) return UNIQUE;
		if (isModernfixFeatureOn("main.IngredientItemValueMixin", mixinClassName, "perf.ingredient_item_deduplication.IngredientItemValueMixin")) return UNIQUE;
		if (isATLFeatureOn(Set.of("main.IngredientSetMixin","main.NormalizedTypedItemStackMixin"), mixinClassName, !ATLProperties.get().ingredientDedupe)) return UNIQUE;
		return annotationNode;
	}

	private boolean isModernfixFeatureOn(String myMixin, String currentMixin, String modernfixMixin){
		if (("dev.uncandango.alltheleaks.mixin.core." + myMixin).equals(currentMixin)) {
			if (LoadingModList.get().getModFileById("modernfix") != null) {
				return ModernFixMixinPlugin.instance.isOptionEnabled(modernfixMixin);
			}
		}
		return false;
	}

	private boolean isATLFeatureOn(Set<String> myMixin, String currentMixin, boolean feature){
		if (myMixin.stream().anyMatch(mixin -> ("dev.uncandango.alltheleaks.mixin.core." + mixin).equals(currentMixin))){
			return feature;
		}
		return false;
	}

	public static final Map<String, Lazy<Integer>> hashCodes = new ConcurrentHashMap<>();
	public static final Map<MethodNode, List<Integer>> compatibleHashes = new ConcurrentHashMap<>();
	public static final Set<String> computingHashes = Sets.newConcurrentHashSet();
	private synchronized static AdjustableAnnotationNode processHashes(List<String> targetClassNames, String mixinClassName, AdjustableAnnotationNode annotationNode,MethodNode handlerNode) {
		if (annotationNode.desc.equals(CompatibleHashes.class.descriptorString())){
			if (targetClassNames.size() > 1) throw new IllegalArgumentException("CompatibleHashes can't be applied to multiple target classes!");
			annotationNode.<List<Integer>>get("values").ifPresentOrElse(hashes -> compatibleHashes.put(handlerNode, hashes),() -> compatibleHashes.put(handlerNode, List.of(0)));
			computingHashes.add(mixinClassName);
			try {
				var classNode = MixinService.getService().getBytecodeProvider().getClassNode(targetClassNames.get(0));
				for (var method : classNode.methods) {
					Lazy<Integer> calcHash = Lazy.of(() -> MethodNodeHasher.hash(classNode, method.name + method.desc));
					var fullName = method.name + method.desc;
					hashCodes.put(fullName, calcHash);
				}
				return annotationNode;
			} catch (ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		}
		if (!computingHashes.contains(mixinClassName)) return annotationNode;

		List<String> methods = null;
		if (annotationNode instanceof MethodListAnnotationNode methodListAnnotationNode) {
			if (!IS_SRG) {
				methodListAnnotationNode.applyRefmap();
			}
			methods = methodListAnnotationNode.getMethod();
		}
		if (annotationNode instanceof AdjustableOverwriteNode) {
			methods = new ArrayList<>(List.of(handlerNode.name + handlerNode.desc));
		}

		if (methods == null) {
			return annotationNode;
		}

		var it = methods.iterator();
		while (it.hasNext()) {
			var method = it.next();
			var searchMethod = method.startsWith("L") && method.indexOf(";m_") > 0 ? method.substring(method.indexOf(";m_") + 1) : method.indexOf('(') == -1 ? method + "(" : method;
			if (searchMethod.indexOf('*') > 0) {
				searchMethod = searchMethod.replace("*","");
			}
			boolean found = false;
			for (var key : hashCodes.keySet()) {
				if (key.startsWith(searchMethod)) {
					found = true;
					var hash = hashCodes.get(key).get();
					AllTheLeaks.LOGGER.debug("Method Hash {} calculated for method {} at mixin {}", hash, method, mixinClassName);
					var expectedHashes = compatibleHashes.get(handlerNode);
					if (!expectedHashes.contains(hash)) {
						AllTheLeaks.LOGGER.info("Method {} at mixin {} cancelled due to not matching hash: Actual {}, Expected: {}",method, mixinClassName, hash, expectedHashes);
						it.remove();
						break;
					}
				}
			}
			if (!found) {
				var ex = new IllegalArgumentException("Method " + searchMethod + " not found in possible methods of the class " + targetClassNames.get(0));
				AllTheLeaks.LOGGER.error(ex.getMessage(), ex);
				methods.clear();
			}
		}
		hashCodes.clear();
		compatibleHashes.clear();
		computingHashes.remove(mixinClassName);
		if (methods.isEmpty()) {
			AllTheLeaks.LOGGER.info("Mixin annotation {} at mixin {} removed due to no methods available", annotationNode, mixinClassName);
			return UNIQUE;
		}
		return annotationNode;
	}

}
