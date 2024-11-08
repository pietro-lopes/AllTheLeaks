package dev.uncandango.alltheleaks.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public interface UpdateableLevel<T> {
	List<WeakReference<UpdateableLevel<?>>> INSTANCES = new ArrayList<>();

	static <O extends UpdateableLevel<?>> void register(O object) {
		synchronized (INSTANCES) {
			INSTANCES.add(new WeakReference<>(object));
		}
	}

	void atl$onClientLevelUpdated(@Nullable ClientLevel level);

	class RenderEnginesUpdated extends Event {
		@Nullable ClientLevel level;

		public RenderEnginesUpdated(@Nullable ClientLevel level) {
			this.level = level;
		}


		public @Nullable ClientLevel getLevel() {
			return level;
		}
	}

	@EventBusSubscriber(Dist.CLIENT)
	class Manager {

		@SubscribeEvent
		static public void onLevelLoad(RenderEnginesUpdated event) {
			synchronized (INSTANCES) {
				var it = INSTANCES.iterator();
				while (it.hasNext()) {
					var instance = it.next().get();
					if (instance != null) {
						instance.atl$onClientLevelUpdated(event.getLevel());
					} else {
						it.remove();
					}
				}
				((ArrayList) INSTANCES).trimToSize();
			}
		}
	}
}
