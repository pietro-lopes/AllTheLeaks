package dev.uncandango.alltheleaks.leaks.common.mods.firespreadtweaks;

import com.natamus.firespreadtweaks_common_forge.events.FireSpreadEvent;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;

import java.lang.invoke.VarHandle;
import java.util.HashMap;

@Issue(modId = "firespreadtweaks", versionRange = "*")
public class UntrackedIssue001 {
	public static final VarHandle FIRE_POSITIONS;

	static {
		FIRE_POSITIONS = ReflectionHelper.getFieldFromClass(FireSpreadEvent.class, "firepositions", HashMap.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(EventPriority.LOWEST,this::clearOnLevelUnload);
	}

	private void clearOnLevelUnload(LevelEvent.Unload event) {
		if (!event.getLevel().isClientSide()) {
			((HashMap)FIRE_POSITIONS.get()).remove(event.getLevel());
		}
	}
}
