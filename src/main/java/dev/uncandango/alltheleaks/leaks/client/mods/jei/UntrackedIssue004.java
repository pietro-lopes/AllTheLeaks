package dev.uncandango.alltheleaks.leaks.client.mods.jei;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.invoke.VarHandle;

@Issue(modId = "jei", versionRange = "[19.22.1.316,)")
public class UntrackedIssue004 {
	public static final VarHandle GRINDSTONE_MENU;

	public UntrackedIssue004() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearMenuOnPlayerClone);
		gameBus.addListener(this::clearMenuOnPlayerLogout);
	}

	static {
		var clazz = ReflectionHelper.getClass("mezz.jei.library.plugins.vanilla.grindstone.GrindstoneRecipeMaker");
		GRINDSTONE_MENU = ReflectionHelper.getFieldFromClass(clazz, "GRINDSTONE_MENU", GrindstoneMenu.class, true);
	}

	private void clearMenuOnPlayerClone(ClientPlayerNetworkEvent.Clone event){
		GRINDSTONE_MENU.set((Object)null);
	}

	private void clearMenuOnPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event){
		GRINDSTONE_MENU.set((Object)null);
	}

}
