package dev.uncandango.alltheleaks.leaks.common.mods.neoforge;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(issueId = "#1487", modId = "neoforge", versionRange = "[21.,)", mixins = {"main.ServerPlayerMixin"}) // "main.FakePlayerFactoryMixin", "main.FakePlayerMixin", "accessor.PlayerListAccessor",
public class Issue1487 {
//	private static final Constructor<?> FAKE_PLAYER_KEY_CTOR;
//	private static final Class<?> FAKE_PLAYER_KEY_CLASS;
//	private static final Map<Record, FakePlayer> fakePlayers;
//
//	static {
//		FAKE_PLAYER_KEY_CLASS = ReflectionHelper.getPrivateClass(FakePlayerFactory.class, "net.neoforged.neoforge.common.util.FakePlayerFactory$FakePlayerKey");
//		FAKE_PLAYER_KEY_CTOR = ReflectionHelper.getRecordCtor(FAKE_PLAYER_KEY_CLASS);
//		VarHandle FAKE_PLAYERS = ReflectionHelper.getFieldFromClass(FakePlayerFactory.class, "fakePlayers", Map.class, true);
//		fakePlayers = (Map<Record, FakePlayer>) ReflectionHelper.getFieldValue(FAKE_PLAYERS);
//	}
//
//	public static void registerFakePlayer(ServerLevel level, GameProfile username, FakePlayer player) {
//		try {
//			FAKE_PLAYER_KEY_CTOR.setAccessible(true);
//			var key = FAKE_PLAYER_KEY_CTOR.newInstance(level, username);
//			fakePlayers.computeIfAbsent((Record) key, k -> player);
//		} catch (Exception e) {
//			AllTheLeaks.LOGGER.error("Error while trying to create instance of FakePlayerKey", e);
//		}
//	}
//
//	public static Predicate<Map.Entry<?, FakePlayer>> stopListeningToAdvancements(ServerLevel level) {
//		return entry -> {
//			if (entry.getValue().level() == level) {
//				var fakePlayer = entry.getValue();
//				fakePlayer.getAdvancements().stopListening();
////				var playerList = fakePlayer.server.getPlayerList();
////				if (playerList instanceof PlayerListAccessor accessor) {
//////					accessor.atl$getAdvancements().remove(fakePlayer.getUUID());
//////					accessor.atl$getStats().remove(fakePlayer.getUUID());
////				}
//				return true;
//			} else {
//				return false;
//			}
//		};
//	}
}
