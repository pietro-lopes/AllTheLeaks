package dev.uncandango.alltheleaks.leaks.client.mods.mekanism;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import mekanism.common.lib.transmitter.TransmitterNetworkRegistry;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

@Issue(modId = "mekanism", versionRange = "[10.7.8,)")
public class UntrackedIssue001 {
	public static final MethodHandle CLEAR_CLIENT_NETWORKS;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearNetworks);
	}

	static {
		CLEAR_CLIENT_NETWORKS = ReflectionHelper.getMethodFromClass(TransmitterNetworkRegistry.class, "clearClientNetworks", MethodType.methodType(void.class), true);
	}

	private void clearNetworks(UpdateableLevel.RenderEnginesUpdated event) {
		try {
			CLEAR_CLIENT_NETWORKS.invoke();
		} catch (Throwable ignored) {}
	}
}
