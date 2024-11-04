package dev.uncandango.alltheleaks.mixin;

import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultimap;
import dev.uncandango.alltheleaks.AllTheLeaks;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public interface UpdateableLevel<T> {
	HashMultimap<Class<?>, WeakReference<UpdateableLevel<?>>> INSTANCES = HashMultimap.create();

	static <O extends UpdateableLevel<?>> void register(O object) {
		synchronized (INSTANCES) {
			INSTANCES.put(object.getClass(), new WeakReference<>(object));
		}
	}

	void atl$onClientLevelUpdated(@Nullable ClientLevel level);

	class RenderEnginesUpdated extends LevelEvent {
		public RenderEnginesUpdated(@Nullable ClientLevel level) {
			super(level);
		}

		@Nullable
		public ClientLevel getLevel() {
			return (ClientLevel) super.getLevel();
		}
	}

	@Mod.EventBusSubscriber(Dist.CLIENT)
	class Manager {

		@SubscribeEvent
		static public void onLevelLoad(UpdateableLevel.RenderEnginesUpdated event) {
			synchronized (INSTANCES) {
				var it = INSTANCES.values().iterator();
				var watch = Stopwatch.createStarted();
				int count = 0;
				while (it.hasNext()) {
					count++;
					var instance = it.next().get();
					if (instance != null) {
						instance.atl$onClientLevelUpdated(event.getLevel());
					} else {
						it.remove();
					}
				}
				AllTheLeaks.LOGGER.debug("Updated {} level instances in {}ms", count, watch.stop().elapsed(TimeUnit.MILLISECONDS));
			}
		}
	}
}
