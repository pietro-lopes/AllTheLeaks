package dev.uncandango.alltheleaks.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.uncandango.alltheleaks.leaks.common.mods.neoforge.UntrackedIssue001;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;
import java.util.function.Predicate;

@Mixin(FakePlayerFactory.class)
public class FakePlayerFactoryMixin {

	@ModifyArg(method = "unloadLevel", at = @At(value = "INVOKE", target = "Ljava/util/Set;removeIf(Ljava/util/function/Predicate;)Z"))
	private static Predicate<Map.Entry<?, FakePlayer>> atl$stopListeningToAdvancements(Predicate<Map.Entry<?, FakePlayer>> predicate, @Local(argsOnly = true) ServerLevel level) {
		return UntrackedIssue001.stopListeningToAdvancements(level);
	}
}
