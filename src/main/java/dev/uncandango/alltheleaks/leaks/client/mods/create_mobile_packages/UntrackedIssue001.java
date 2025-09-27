package dev.uncandango.alltheleaks.leaks.client.mods.create_mobile_packages;

import de.theidler.create_mobile_packages.CreateMobilePackages;
import de.theidler.create_mobile_packages.RoboManager;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "create_mobile_packages", versionRange = "*")
public class UntrackedIssue001 {
	public static final VarHandle CLIENT_ROBOS;

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearOnRenderEngineUpdate);
	}

	static {
		var dummy = CreateMobilePackages.ROBO_MANAGER;
		CLIENT_ROBOS = ReflectionHelper.getFieldFromClass(RoboManager.class, "clientRobos", Map.class, false);
	}

	private void clearOnRenderEngineUpdate(UpdateableLevel.RenderEnginesUpdated event) {
		((Map)CLIENT_ROBOS.get(CreateMobilePackages.ROBO_MANAGER)).clear();
	}
}
