package dev.uncandango.alltheleaks.leaks.client.mods.sereneseasons;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.Level;

import java.lang.invoke.VarHandle;

@Issue(modId = "sereneseasons", versionRange = "[10.1.0.1,)")
public class UntrackedIssue001 implements UpdateableLevel<UntrackedIssue001> {
	public static final VarHandle renderSnowAndRain_level;
	public static UntrackedIssue001 INSTANCE;

	static {
		renderSnowAndRain_level = ReflectionHelper.getFieldFromClass(LevelRenderer.class, "renderSnowAndRain_level", Level.class, false);
	}

	public UntrackedIssue001() {
		INSTANCE = this;
		UpdateableLevel.register(INSTANCE);
	}

	@Override
	public void onClientLevelUpdated(ClientLevel level) {
		renderSnowAndRain_level.set(Minecraft.getInstance().levelRenderer, level);
	}
}
