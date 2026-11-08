package dev.uncandango.alltheleaks.leaks.client.mods.touhou_little_maid;

import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraftforge.common.MinecraftForge;

@Issue(modId = "touhou_little_maid", versionRange = "[1.5.3-forge,)")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::onRenderEngineUpdate);
	}

	static {
		var dummy = EntityCacheUtil.ENTITY_CACHE;
	}

	private void onRenderEngineUpdate(UpdateableLevel.RenderEnginesUpdated event) {
		EntityCacheUtil.ENTITY_CACHE.invalidateAll();
	}
}
