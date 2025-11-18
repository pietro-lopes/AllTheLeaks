package dev.uncandango.alltheleaks.leaks.client.mods.irons_spellbooks;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingMusicManager;
import net.minecraftforge.common.MinecraftForge;

import java.lang.invoke.MethodType;

@Issue(modId = "irons_spellbooks", versionRange = "[1.20.1-3.4.0,)",
	description = "Clears `DeadKingMusicManager#INSTANCE` on client level update")
public class UntrackedIssue002 {
	public UntrackedIssue002() {
		var gamebus = MinecraftForge.EVENT_BUS;
		gamebus.addListener(this::clearDeadKingMusicManager);
	}

	static {
		var dummy = ReflectionHelper.getMethodFromClass(DeadKingMusicManager.class, "hardStop", MethodType.methodType(void.class), true);
	}

	private void clearDeadKingMusicManager(UpdateableLevel.RenderEnginesUpdated event) {
		DeadKingMusicManager.hardStop();
	}
}
