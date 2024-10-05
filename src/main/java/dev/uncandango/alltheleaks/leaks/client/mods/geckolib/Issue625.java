package dev.uncandango.alltheleaks.leaks.client.mods.geckolib;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import software.bernie.geckolib.loading.math.MolangQueries;

import java.lang.invoke.VarHandle;

@Issue(modId = "geckolib", versionRange = "[4.6,4.6.2]", mixins = "main.GeoArmorRendererMixin")
public class Issue625 {
	private static final VarHandle ACTOR;
	public Issue625() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearActor);
	}

	private void clearActor(LevelEvent.Unload event){
		ACTOR.set((Object)null);
	}

	static {
		ACTOR = ReflectionHelper.getFieldFromClass(MolangQueries.class, "ACTOR", MolangQueries.Actor.class, true);
	}
}
