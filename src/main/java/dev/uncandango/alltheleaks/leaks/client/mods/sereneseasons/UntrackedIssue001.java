package dev.uncandango.alltheleaks.leaks.client.mods.sereneseasons;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

import java.lang.invoke.VarHandle;

@Issue(modId = "sereneseasons", versionRange = "[9.1.0.0,)", description = "Clears mixin added field `LevelRenderer#renderSnowAndRain_level` on client level update")
public class UntrackedIssue001 {
	public static final VarHandle renderSnowAndRain_level;

	static {
		renderSnowAndRain_level = ReflectionHelper.getFieldFromClass(LevelRenderer.class, "renderSnowAndRain_level", Level.class, false);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::updateLevelRenderer);
	}

	private void updateLevelRenderer(UpdateableLevel.RenderEnginesUpdated event) {
		renderSnowAndRain_level.set(Minecraft.getInstance().levelRenderer, event.getLevel());
	}
}
