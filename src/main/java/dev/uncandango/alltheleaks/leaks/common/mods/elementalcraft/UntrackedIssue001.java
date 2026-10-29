package dev.uncandango.alltheleaks.leaks.common.mods.elementalcraft;

import com.google.common.collect.Multimap;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import sirttas.elementalcraft.block.shrine.enderlock.EnderLockHandler;

import java.lang.invoke.VarHandle;

@Issue(modId = "elementalcraft", versionRange = "[7.1.3,)")
public class UntrackedIssue001 {
	public static final VarHandle ENDER_LOCK_SHRINES;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearOnLevelUnload);
	}

	static {
		ENDER_LOCK_SHRINES = ReflectionHelper.getFieldFromClass(EnderLockHandler.class, "ENDER_LOCK_SHRINES", Multimap.class, true);
	}

	private void clearOnLevelUnload(LevelEvent.Unload event){
		if (event.getLevel() instanceof Level level) {
			((Multimap)ENDER_LOCK_SHRINES.get()).removeAll(level);
		}
	}
}
