package dev.uncandango.alltheleaks.feature.common.mods.minecraft;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppedEvent;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Issue(modId = "minecraft", issueId = "ResourceLocation Deduplication", versionRange = "1.20.1", mixins = {"main.ResourceLocationMixin"}, config = "resourceLocationDedupe",
	description = "Deduplicates ResourceLocations, should be tested to check if it is worth for your case")
public class ResourceLocationDedupe {
	public static final AtomicLong TIMER = new AtomicLong();
	private static final ObjectOpenCustomHashSet<String> NAMESPACE_CACHE;
	private static final ObjectOpenCustomHashSet<String> PATH_CACHE;

	static {
		var BASIC_HASH_STRATEGY = new Hash.Strategy<String>() {
			@Override
			public int hashCode(String o) {
				return Objects.hashCode(o);
			}

			@Override
			public boolean equals(String a, String b) {
				return Objects.equals(a, b);
			}
		};
		NAMESPACE_CACHE = new ObjectOpenCustomHashSet<>(BASIC_HASH_STRATEGY);
		PATH_CACHE = new ObjectOpenCustomHashSet<>(BASIC_HASH_STRATEGY);
	}

	public ResourceLocationDedupe() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::printTimer);
	}

	private void printTimer(ServerStoppedEvent event) {
		AllTheLeaks.LOGGER.info("ResourceLocationDedupe: " + TIMER.get() / 1_000_000 + "ms");
	}

	public synchronized static String internNamespace(String namespace) {
		return NAMESPACE_CACHE.addOrGet(namespace);
	}

	public synchronized static String internPath(String path) {
		return PATH_CACHE.addOrGet(path);
	}
}
