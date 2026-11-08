package dev.uncandango.alltheleaks.leaks.common.mods.mts;

import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

@Issue(modId = "mts", versionRange = "[24.0.0,)", mixins = {"main.BuilderEntityRenderForwarderMixin"})
public class UntrackedIssue002 {
	public static boolean isEntityInvalid(Entity entity){
		if (entity.level().isClientSide()) {
			return entity.level() != Minecraft.getInstance().level;
		} else return !entity.isAlive();
	}

}
