package dev.uncandango.alltheleaks.leaks.client.mods.jei;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.invoke.VarHandle;

@Issue(modId = "jei", versionRange = "[19.32.0.358,19.70)")
public class UntrackedIssue006 {
	public static final VarHandle GRINDSTONE_MENU;
	public static final VarHandle ANVIL_MENU;

	public UntrackedIssue006() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearMenuOnPlayerClone);
		gameBus.addListener(this::clearMenuOnPlayerLogout);
	}

	static {
		var clazz = ReflectionHelper.getClass("mezz.jei.library.plugins.vanilla.grindstone.GrindstoneHelper");
		GRINDSTONE_MENU = ReflectionHelper.getFieldFromClass(clazz, "GRINDSTONE_MENU", GrindstoneMenu.class, true);
		var clazz2 = ReflectionHelper.getClass("mezz.jei.library.plugins.vanilla.anvil.AnvilHelper");
		ANVIL_MENU = ReflectionHelper.getFieldFromClass(clazz2, "ANVIL_MENU", AnvilMenu.class, true);
	}

	private void clearMenuOnPlayerClone(ClientPlayerNetworkEvent.Clone event){
		GRINDSTONE_MENU.set((Object)null);
		ANVIL_MENU.set((Object)null);
	}

	private void clearMenuOnPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event){
		GRINDSTONE_MENU.set((Object)null);
		ANVIL_MENU.set((Object)null);
	}

}
