package dev.uncandango.alltheleaks.leaks.client.mods.cookingforblockheads;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.blay09.mods.cookingforblockheads.client.render.CowJarRenderer;
import net.minecraft.world.entity.animal.Cow;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.invoke.VarHandle;

@Issue(modId = "cookingforblockheads", versionRange = "[21.1.14,)")
public class UntrackedIssue001 {
	public static final VarHandle ENTITY;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearEntities);
	}

	static {
		ENTITY = ReflectionHelper.getFieldFromClass(CowJarRenderer.class, "entity", Cow.class, true);
	}

	private void clearEntities(UpdateableLevel.RenderEnginesUpdated event) {
		ENTITY.set((Object) null);
	}
}
