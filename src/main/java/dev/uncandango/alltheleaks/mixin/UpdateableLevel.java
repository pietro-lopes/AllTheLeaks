package dev.uncandango.alltheleaks.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

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

	void onClientLevelUpdated(ClientLevel level);

	@EventBusSubscriber(Dist.CLIENT)
	class Manager {

		@SubscribeEvent
		static public void onLevelLoad(LevelEvent.Load event) {
			if (event.getLevel().isClientSide()) {
				synchronized (INSTANCES) {
					var it = INSTANCES.iterator();
					while (it.hasNext()) {
						var instance = it.next().get();
						if (instance != null) {
							instance.onClientLevelUpdated((ClientLevel) event.getLevel());
						} else it.remove();
					}
				}
			}
		}
	}
}
