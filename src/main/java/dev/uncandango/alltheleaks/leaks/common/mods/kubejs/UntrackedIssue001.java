package dev.uncandango.alltheleaks.leaks.common.mods.kubejs;

import dev.latvian.mods.kubejs.bindings.event.ServerEvents;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.ServerScriptManagerExtension;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

@Issue(modId = "kubejs", versionRange = "2101.7.0-build.171", mixins = {"main.ServerScriptManagerMixin", "accessor.ContextFactoryAccessor"})
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearContext);
	}

	private void clearContext(ServerStoppedEvent event) {
		var manager = event.getServer().getServerResources().managers().kjs$getServerScriptManager();
		if (manager != null) {
			if (manager instanceof ServerScriptManagerExtension extension) {
				extension.clearContext();
			}
		}
		ServerEvents.GROUP.getHandlers().values().forEach(handler -> handler.clear(ScriptType.SERVER));
	}
}
