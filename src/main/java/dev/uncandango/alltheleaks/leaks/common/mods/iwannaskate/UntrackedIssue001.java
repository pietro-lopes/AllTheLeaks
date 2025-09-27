package dev.uncandango.alltheleaks.leaks.common.mods.iwannaskate;

import com.github.alexthe668.iwannaskate.server.world.IWSWorldData;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "iwannaskate", versionRange = "*", description = "Remove unloaded level from `IWSWorldData#dataMap`")
public class UntrackedIssue001 {
	public static final VarHandle DATA_MAP;

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearLevelOnUnload);
	}

	private void clearLevelOnUnload(LevelEvent.Unload event) {
		if (!event.getLevel().isClientSide()){
			((Map)DATA_MAP.get()).remove(event.getLevel());
		}
	}

	static {
		DATA_MAP = ReflectionHelper.getFieldFromClass(IWSWorldData.class, "dataMap", Map.class, true);
	}
}
