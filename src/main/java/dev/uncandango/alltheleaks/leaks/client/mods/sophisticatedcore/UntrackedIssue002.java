package dev.uncandango.alltheleaks.leaks.client.mods.sophisticatedcore;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.p3pp3rf1y.sophisticatedcore.compat.craftingtweaks.CraftingUpgradeTweakUIPart;

import java.lang.invoke.MethodType;

@Issue(modId = "sophisticatedcore", versionRange = "[0.6.23,)", extraModDep = {"craftingtweaks"}, extraModDepVersions = {"[21.1.1,)"})
public class UntrackedIssue002 {

	public UntrackedIssue002() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearInventoryMenu);
		gameBus.addListener(this::clearInventoryMenuOnClearLevel);
	}

	static {
		var dummy = ReflectionHelper.getMethodFromClass(CraftingUpgradeTweakUIPart.class, "register", MethodType.methodType(void.class), true);
	}

	private void clearInventoryMenuOnClearLevel(UpdateableLevel.RenderEnginesUpdated event) {
		if (event.getLevel() == null) {
			CraftingUpgradeTweakUIPart.register();
		}
	}

	private void clearInventoryMenu(ClientPlayerNetworkEvent.Clone event) {
		CraftingUpgradeTweakUIPart.register();
	}
}
