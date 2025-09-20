package dev.uncandango.alltheleaks.leaks.client.mods.draconicevolution;

import com.brandon3055.draconicevolution.items.MobSoul;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "draconicevolution", versionRange = "*")
public class UntrackedIssue001 {
	public static final VarHandle RENDER_ENTITY_MAP;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearEntityMap);
	}

	static {
		RENDER_ENTITY_MAP = ReflectionHelper.getFieldFromClass(MobSoul.class, "renderEntityMap", Map.class, true);
	}

	private void clearEntityMap(UpdateableLevel.RenderEnginesUpdated event) {
		((Map)RENDER_ENTITY_MAP.get()).clear();
	}
}
