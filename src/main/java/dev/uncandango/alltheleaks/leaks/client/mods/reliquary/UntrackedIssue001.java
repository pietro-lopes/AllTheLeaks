package dev.uncandango.alltheleaks.leaks.client.mods.reliquary;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import reliquary.util.TooltipBuilder;

import java.lang.invoke.VarHandle;

@Issue(modId = "reliquary", versionRange = "[2.0.44,)")
public class UntrackedIssue001 {
	public static final VarHandle CONTEXT;

	static {
		CONTEXT = ReflectionHelper.getFieldFromClass(TooltipBuilder.class, "context", Item.TooltipContext.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::refreshContext);
		gameBus.addListener(this::clearContext);
	}

	private void refreshContext(LevelEvent.Load event) {
		if (event.getLevel().isClientSide()) {
			CONTEXT.set(Item.TooltipContext.of((Level) event.getLevel()));
		}
	}

	private void clearContext(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
			CONTEXT.set(Item.TooltipContext.EMPTY);
		}
	}
}
