package dev.uncandango.alltheleaks.leaks.common.mods.arknights_endfield;

import com.besson.endfield.util.power.PowerNetworkManager;
import com.besson.endfield.util.power.PowerNetworkNodeManager;
import com.besson.endfield.util.storage.GlobalStorageManager;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "arknights_endfield", versionRange = "[1.0.3-1.20.1,)")
public class UntrackedIssue001 {
	public static final VarHandle INSTANCE;
	public static final VarHandle INSTANCE2;
	public static final VarHandle INSTANCE3;

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearOnUnload);
	}

	private void clearOnUnload(LevelEvent.Unload event) {
		if (event.getLevel() instanceof ServerLevel level) {
			var instance = (Map<ServerLevel, PowerNetworkManager>)INSTANCE.get();
			instance.remove(level);
			var instance2 = (Map<ServerLevel, PowerNetworkNodeManager>)INSTANCE2.get();
			instance2.remove(level);
			var instance3 = (Map<ServerLevel, GlobalStorageManager>)INSTANCE3.get();
			instance3.remove(level);
		}
	}

	static {
		INSTANCE = ReflectionHelper.getFieldFromClass(PowerNetworkManager.class, "INSTANCE", Map.class, true);
		INSTANCE2 = ReflectionHelper.getFieldFromClass(PowerNetworkNodeManager.class, "INSTANCE", Map.class, true);
		INSTANCE3 = ReflectionHelper.getFieldFromClass(GlobalStorageManager.class, "INSTANCE", Map.class, true);
	}
}
