package dev.uncandango.alltheleaks.mixin.core.plugin;

import com.bawnorton.mixinsquared.adjuster.MixinAnnotationAdjusterRegistrar;
import com.bawnorton.mixinsquared.canceller.MixinCancellerRegistrar;
import dev.uncandango.alltheleaks.fix.common.mods.kubejs.FixItemStackModification;
import dev.uncandango.alltheleaks.leaks.IssueManager;
import dev.uncandango.alltheleaks.mixinsq.ATLMixinAdjuster;
import dev.uncandango.alltheleaks.mixinsq.ATLMixinCanceller;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ATLMixinPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {
		MixinAnnotationAdjusterRegistrar.register(new ATLMixinAdjuster());
		MixinCancellerRegistrar.register(new ATLMixinCanceller());
		DebugThreadsStuck.start();
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
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
		if (mixinClassName.equals("dev.uncandango.alltheleaks.mixin.core.main.IngredientKJSMixin")) {
			FixItemStackModification.transformClass(targetClass);
		}
	}

	@Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
