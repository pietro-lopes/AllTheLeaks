package dev.uncandango.alltheleaks.leaks.client.mods.watut;

import com.corosus.watut.PlayerStatusManagerClient;
import com.corosus.watut.client.CustomParticleEngine;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.common.MinecraftForge;

import java.lang.invoke.MethodType;

@Issue(modId = "watut", versionRange = "*", description = "Update level from CustomParticleEngine")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::updateLevel);
	}

	static {
		var dummy = ReflectionHelper.getMethodFromClass(PlayerStatusManagerClient.class, "getParticleEngine", MethodType.methodType(CustomParticleEngine.class), true);
		var dummy2 = ReflectionHelper.getMethodFromClass(CustomParticleEngine.class, "setLevel", MethodType.methodType(void.class, ClientLevel.class), false);
	}

	private void updateLevel(UpdateableLevel.RenderEnginesUpdated event) {
		PlayerStatusManagerClient.getParticleEngine().setLevel(event.getLevel());
	}
}
