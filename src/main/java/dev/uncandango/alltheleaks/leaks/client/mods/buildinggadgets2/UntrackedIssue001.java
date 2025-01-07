package dev.uncandango.alltheleaks.leaks.client.mods.buildinggadgets2;

import com.direwolf20.buildinggadgets2.client.renderer.VBORenderer;
import com.direwolf20.buildinggadgets2.util.FakeRenderingWorld;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.invoke.VarHandle;

@Issue(modId = "buildinggadgets2", versionRange = "[1.3.7,)")
public class UntrackedIssue001 {
	public static final VarHandle FAKE_RENDERING_WORLD;
	public static final VarHandle REAL_WORLD;

	static {
		FAKE_RENDERING_WORLD = ReflectionHelper.getFieldFromClass(VBORenderer.class, "fakeRenderingWorld", FakeRenderingWorld.class, true);
		REAL_WORLD = ReflectionHelper.getFieldFromClass(FakeRenderingWorld.class, "realWorld", Level.class, false);
	}

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearFakeWorld);
	}

	private void clearFakeWorld(UpdateableLevel.RenderEnginesUpdated event) {
		if (event.getLevel() == null) {
			FAKE_RENDERING_WORLD.set((Object) null);
		} else {
			var world = FAKE_RENDERING_WORLD.get();
			if (world != null) {
				REAL_WORLD.set(world, event.getLevel());
			}
		}
	}
}
