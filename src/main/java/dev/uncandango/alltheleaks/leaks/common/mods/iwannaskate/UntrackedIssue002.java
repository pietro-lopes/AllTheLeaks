package dev.uncandango.alltheleaks.leaks.common.mods.iwannaskate;

import com.github.alexthe668.iwannaskate.server.CommonProxy;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "iwannaskate", versionRange = "*", description = "Remove unloaded level from `CommonProxy#WANDERING_SKATER_SPAWNER_MAP`")
public class UntrackedIssue002 {
	public static final VarHandle WANDERING_SKATER_SPAWNER_MAP;

	public UntrackedIssue002() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearLevelOnUnload);
	}

	private void clearLevelOnUnload(LevelEvent.Unload event) {
		if (!event.getLevel().isClientSide()){
			((Map)WANDERING_SKATER_SPAWNER_MAP.get()).remove(event.getLevel());
		}
	}

	static {
		WANDERING_SKATER_SPAWNER_MAP = ReflectionHelper.getFieldFromClass(CommonProxy.class, "WANDERING_SKATER_SPAWNER_MAP", Map.class, true);
	}
}
