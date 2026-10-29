package dev.uncandango.alltheleaks.leaks.client.mods.primalmagick;

import com.verdantartifice.primalmagick.common.research.ResearchManager;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.VarHandle;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Issue(modId = "primalmagick", versionRange = "[6.0.7,)")
public class UntrackedIssue001 {
	public static final VarHandle memoizedAffinityIndexEntries;
	public static final VarHandle memoizedCache;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::onPlayerClone);
		gameBus.addListener(this::onPlayerLogout);
	}
	static {
		memoizedAffinityIndexEntries = ReflectionHelper.getFieldFromClass(ResearchManager.class, "memoizedAffinityIndexEntries", Function.class, true);
		try {
			var memoizedClass = Class.forName("net.minecraft.Util$8");
			memoizedCache = ReflectionHelper.getFieldFromClass(memoizedClass, "cache", Map.class, false);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	private void onPlayerClone(ClientPlayerNetworkEvent.Clone event) {
		clear(event.getNewPlayer());
	}

	private static void clear(@Nullable Player player) {
		var memoizedObj = (Function<?,?>)memoizedAffinityIndexEntries.get();
		var cache = (Map<Player, List<?>>)memoizedCache.get(memoizedObj);
		if (player == null) {
			cache.clear();
		} else {
			var it = cache.entrySet().iterator();
			List<?> value = null;
			while (it.hasNext()) {
				var entry = it.next();
				if (entry.getKey().getUUID().equals(player.getUUID())) {
					value = entry.getValue();
					it.remove();
					break;
				}
			}
			if (value != null) {
				cache.put(player, value);
			}
		}
	}

	private void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event){
		clear(null);
	}
}
