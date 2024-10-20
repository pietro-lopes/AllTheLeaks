package dev.uncandango.alltheleaks.leaks.client.mods.gtceu;

import com.gregtechceu.gtceu.api.gui.widget.PatternPreviewWidget;
import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.Event;

import java.lang.invoke.VarHandle;

@Issue(modId = "gtceu", versionRange = "[1.2.0.a,)")
public class UntrackedIssue001 {
	public static final VarHandle LEVEL;

	static {
		LEVEL = ReflectionHelper.getFieldFromClass(PatternPreviewWidget.class, "LEVEL", TrackedDummyWorld.class, true);
	}
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::updateLevel);
	}

	private void updateLevel(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
			LEVEL.set((Object) null);
		}
	}
}
