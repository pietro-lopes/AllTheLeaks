package dev.uncandango.alltheleaks.leaks.client.mods.ldlib;

import com.gregtechceu.gtceu.api.gui.widget.PatternPreviewWidget;
import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.Event;

@Issue(modId = "ldlib", versionRange = "[1.0.26.b,)", mixins = "main.DummyWorldMixin")
public class UntrackedIssue001 {
}
