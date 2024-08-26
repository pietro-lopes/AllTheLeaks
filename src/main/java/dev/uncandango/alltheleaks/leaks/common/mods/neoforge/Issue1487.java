package dev.uncandango.alltheleaks.leaks.common.mods.neoforge;

import com.mojang.authlib.GameProfile;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.PlayerListAccessor;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;

import java.lang.invoke.VarHandle;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.function.Predicate;

@Issue(modId = "neoforge", versionRange = "[21.,)", mixins = {"main.FakePlayerFactoryMixin", "main.FakePlayerMixin", "accessor.PlayerListAccessor"})
public class UntrackedIssue001 {
	private static final Constructor<?> FAKE_PLAYER_KEY_CTOR;
	private static final Class<?> FAKE_PLAYER_KEY_CLASS;
	private static final Map<Record, FakePlayer> fakePlayers;

	static {
		FAKE_PLAYER_KEY_CLASS = ReflectionHelper.getPrivateClass(FakePlayerFactory.class, "net.neoforged.neoforge.common.util.FakePlayerFactory$FakePlayerKey");
		FAKE_PLAYER_KEY_CTOR = ReflectionHelper.getRecordCtor(FAKE_PLAYER_KEY_CLASS);
		VarHandle FAKE_PLAYERS = ReflectionHelper.getFieldFromClass(FakePlayerFactory.class, "fakePlayers", Map.class, true);
		fakePlayers = (Map<Record, FakePlayer>) ReflectionHelper.getFieldValue(FAKE_PLAYERS);
	}

	public static void registerFakePlayer(ServerLevel level, GameProfile username, FakePlayer player) {
		try {
			FAKE_PLAYER_KEY_CTOR.setAccessible(true);
			var key = FAKE_PLAYER_KEY_CTOR.newInstance(level, username);
			fakePlayers.computeIfAbsent((Record) key, k -> player);
		} catch (Exception e) {
			AllTheLeaks.LOGGER.error("Error while trying to create instance of FakePlayerKey", e);
		}
	}

	public static Predicate<Map.Entry<?, FakePlayer>> stopListeningToAdvancements(ServerLevel level) {
		return entry -> {
			if (entry.getValue().level() == level) {
				var fakePlayer = entry.getValue();
				fakePlayer.getAdvancements().stopListening();
//				var playerList = fakePlayer.server.getPlayerList();
//				if (playerList instanceof PlayerListAccessor accessor) {
////					accessor.atl$getAdvancements().remove(fakePlayer.getUUID());
////					accessor.atl$getStats().remove(fakePlayer.getUUID());
//				}
				return true;
			} else {
				return false;
			}
		};
	}
}
