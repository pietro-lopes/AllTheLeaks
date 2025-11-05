package dev.uncandango.alltheleaks.mixin.core.plugin;

import com.bawnorton.mixinsquared.canceller.MixinCancellerRegistrar;
import dev.uncandango.alltheleaks.leaks.IssueManager;
import dev.uncandango.alltheleaks.mixinsq.ATLMixinCanceller;
import org.apache.commons.lang3.stream.Streams;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ATLMixinPlugin implements IMixinConfigPlugin {
	private static boolean containsReviveInst(AbstractInsnNode instruction) {
		if (instruction instanceof MethodInsnNode mInst) {
			return mInst.name.equals("revive");
		}
		return false;
	}

	@Override
	public void onLoad(String mixinPackage) {
		MixinCancellerRegistrar.register(new ATLMixinCanceller());
		DebugThreadsStuck.start();
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		AtomicBoolean result = new AtomicBoolean(IssueManager.getAllowedMixins().contains(mixinClassName.replace("dev.uncandango.alltheleaks.mixin.core.", "")));
		if (result.get()) {
			if (targetClassName.equals("top.theillusivec4.curios.common.event.CuriosEventHandler")) {
				try {
					var classNode = MixinService.getService().getBytecodeProvider().getClassNode("top.theillusivec4.curios.common.event.CuriosEventHandler");
					classNode.methods.stream().filter(m -> m.name.equals("playerClone")).findFirst()
						.ifPresent(methodNode -> {
							result.set(Streams.of(methodNode.instructions.toArray()).anyMatch(ATLMixinPlugin::containsReviveInst));
						});
				} catch (ClassNotFoundException | IOException e) {
					result.set(false);
				}
			}
		}
		return result.get();
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
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}
}
