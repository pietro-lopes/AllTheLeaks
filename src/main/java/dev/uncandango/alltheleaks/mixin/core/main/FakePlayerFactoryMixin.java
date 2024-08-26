package dev.uncandango.alltheleaks.mixin.core.main;

import net.neoforged.neoforge.common.util.FakePlayerFactory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FakePlayerFactory.class)
public class FakePlayerFactoryMixin {

//	@ModifyArg(method = "unloadLevel", at = @At(value = "INVOKE", target = "Ljava/util/Set;removeIf(Ljava/util/function/Predicate;)Z"))
//	private static Predicate<Map.Entry<?, FakePlayer>> atl$stopListeningToAdvancements(Predicate<Map.Entry<?, FakePlayer>> predicate, @Local(argsOnly = true) ServerLevel level) {
//		return Issue1487.stopListeningToAdvancements(level);
//	}
}
