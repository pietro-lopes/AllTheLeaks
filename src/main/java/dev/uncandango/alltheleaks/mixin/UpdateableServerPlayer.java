package dev.uncandango.alltheleaks.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public interface UpdateableServerPlayer<T> {
	List<WeakReference<UpdateableServerPlayer<?>>> INSTANCES = new ArrayList<>();

	static <O extends UpdateableServerPlayer<?>> void register(O object) {
		synchronized (INSTANCES) {
			INSTANCES.add(new WeakReference<>(object));
		}
	}

	void atl$onServerPlayerUpdated(ServerPlayer player);

	@EventBusSubscriber(Dist.CLIENT)
	class Manager {

		@SubscribeEvent
		static public void onPlayerClone(PlayerEvent.Clone event) {
			synchronized (INSTANCES) {
				var it = INSTANCES.iterator();
				while (it.hasNext()) {
					var instance = it.next().get();
					if (instance != null) {
						instance.atl$onServerPlayerUpdated((ServerPlayer) event.getEntity());
					} else it.remove();
				}
				((ArrayList)INSTANCES).trimToSize();
			}
		}
	}
}
