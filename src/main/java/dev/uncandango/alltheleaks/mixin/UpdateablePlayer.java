package dev.uncandango.alltheleaks.mixin;

import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultimap;
import dev.uncandango.alltheleaks.AllTheLeaks;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public interface UpdateablePlayer<T> {
	HashMultimap<Class<?>, WeakReference<UpdateablePlayer<?>>> INSTANCES = HashMultimap.create();

	static <O extends UpdateablePlayer<?>> void register(O object) {
		synchronized (INSTANCES) {
			INSTANCES.put(object.getClass(), new WeakReference<>(object));
		}
	}

	void atl$onClientPlayerUpdated(LocalPlayer player);

	@Mod.EventBusSubscriber(Dist.CLIENT)
	class Manager {

		@SubscribeEvent
		static public void onClientClone(ClientPlayerNetworkEvent.Clone event) {
			synchronized (INSTANCES) {
				var it = INSTANCES.values().iterator();
				var watch = Stopwatch.createStarted();
				int count = 0;
				while (it.hasNext()) {
					count++;
					var instance = it.next().get();
					if (instance != null) {
						instance.atl$onClientPlayerUpdated(event.getNewPlayer());
					} else {
						it.remove();
					}
				}
				AllTheLeaks.LOGGER.debug("Updated {} player instances in {}ms", count, watch.stop().elapsed(TimeUnit.MILLISECONDS));
			}
		}
	}
}
