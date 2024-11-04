package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.sugar.Local;
import dev.uncandango.alltheleaks.leaks.common.mods.forge.UntrackedIssue001;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.UUID;

@Mixin(PlayerList.class)
public class PlayerListMixin {

	@ModifyReceiver(method = "getPlayerAdvancements", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
	private Map<UUID, PlayerAdvancements> atl$replaceWithFakeMapForPut(Map<UUID, PlayerAdvancements> instance, Object k, Object v, @Local(argsOnly = true) ServerPlayer player) {
		if (player instanceof FakePlayer){
			return UntrackedIssue001.atl$fakeAdvancements;
		} else return instance;
	}

	@ModifyReceiver(method = "getPlayerAdvancements", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
	private Map<UUID, PlayerAdvancements> atl$replaceWithFakeMapForGet(Map<UUID, PlayerAdvancements> instance, Object o, @Local(argsOnly = true) ServerPlayer player) {
		if (player instanceof FakePlayer){
			return UntrackedIssue001.atl$fakeAdvancements;
		} else return instance;
	}
}
