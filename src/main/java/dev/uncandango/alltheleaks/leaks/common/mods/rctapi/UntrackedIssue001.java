package dev.uncandango.alltheleaks.leaks.common.mods.rctapi;

import com.gitlab.srcmc.rctapi.api.RCTApi;
import com.gitlab.srcmc.rctapi.api.trainer.Trainer;
import com.gitlab.srcmc.rctapi.api.trainer.TrainerPlayer;
import com.gitlab.srcmc.rctapi.api.trainer.TrainerRegistry;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.lang.invoke.MethodType;
import java.util.stream.Stream;

@Issue(modId = "rctapi", versionRange = "[0.14.5-beta,)")
public class UntrackedIssue001 {
	static {
		var dummy1 = ReflectionHelper.getMethodFromClass(RCTApi.class, "getInstances", MethodType.methodType(Stream.class), true);
		var dummy2 = ReflectionHelper.getMethodFromClass(RCTApi.class, "getTrainerRegistry", MethodType.methodType(TrainerRegistry.class), false);
		var dummy3 = ReflectionHelper.getMethodFromClass(TrainerRegistry.class, "getId", MethodType.methodType(String.class, LivingEntity.class), false);
		var dummy4 = ReflectionHelper.getMethodFromClass(TrainerRegistry.class, "getById", MethodType.methodType(Trainer.class, String.class), false);
		var dummy5 = ReflectionHelper.getMethodFromClass(TrainerRegistry.class, "unregisterById", MethodType.methodType(Trainer.class, String.class), false);
		var dummy6 = ReflectionHelper.getMethodFromClass(TrainerRegistry.class, "registerPlayer", MethodType.methodType(TrainerPlayer.class, String.class, ServerPlayer.class), false);
	}

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::refreshTrainersOnClone);
	}

	private void refreshTrainersOnClone(PlayerEvent.Clone event) {
		if (event.getEntity() instanceof ServerPlayer serverPlayer) {
			RCTApi.getInstances().forEach(instance -> {
				var registry = instance.getValue().getTrainerRegistry();
				var id = registry.getId(event.getOriginal());
				if (id != null && registry.getById(id).getEntity().isRemoved()) {
					registry.unregisterById(id);
					registry.registerPlayer(id, serverPlayer);
				}
			});
		}
	}
}
