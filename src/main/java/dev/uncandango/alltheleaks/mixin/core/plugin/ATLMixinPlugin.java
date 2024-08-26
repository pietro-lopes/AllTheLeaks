package dev.uncandango.alltheleaks.mixin.core.plugin;

import dev.uncandango.alltheleaks.leaks.IssueManager;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ATLMixinPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {

	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
//		if (mixinClassName.contains("NeoForgeServerSparkPluginMixin")) return true;
		return IssueManager.getAllowedMixins().contains(mixinClassName.replace("dev.uncandango.alltheleaks.mixin.core.", ""));
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
//		if (mixinClassName.contains("DroneEntityMixin")){
//			targetClass.methods.stream()
//				.filter(m -> m.name.contains("onSemiblockEvent"))
//				.forEach(m -> {
//					var gameBusNode = new AnnotationNode("Lnet/neoforged/bus/api/SubscribeEvent;");
//					if (m.visibleAnnotations == null) {
//						m.visibleAnnotations = List.of(gameBusNode);
//					} else {
//						m.visibleAnnotations.add(gameBusNode);
//					}
//				});
////			targetClass.methods.forEach(method -> {
////				var annotations = method.visibleAnnotations;
////				if (annotations != null) {
////					annotations.removeIf(a -> a.desc.contains("SubscribeEvent"));
////					if (annotations.isEmpty()) method.visibleAnnotations = null;
////				}
////			});
//		}
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}
}
