package dev.uncandango.alltheleaks.leaks.client.mods.sophisticatedbackpacks;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.p3pp3rf1y.sophisticatedcore.inventory.ItemStackKey;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "sophisticatedcore", versionRange = "[0.6.23,)")
public class UntrackedIssue001 {
	public static final VarHandle CACHE;

	static {
		CACHE = ReflectionHelper.getFieldFromClass(ItemStackKey.class, "CACHE", Map.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearItemStackCache);
	}

	private void clearItemStackCache(ScreenEvent.Closing event) {
		if (Minecraft.getInstance().player != null) {
			var cacheMap = (Map<ItemStack, ItemStackKey>) CACHE.get();
			cacheMap.clear();
		}
	}
}
