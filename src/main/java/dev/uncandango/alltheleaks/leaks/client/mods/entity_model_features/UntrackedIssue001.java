package dev.uncandango.alltheleaks.leaks.client.mods.entity_model_features;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.common.NeoForge;
import traben.entity_model_features.models.animation.EMFAnimationEntityContext;

import java.lang.invoke.VarHandle;

@Issue(modId = "entity_model_features", versionRange = "[2.2.6,)")
public class UntrackedIssue001 {
	public static final VarHandle EMF$HELDITERATION;

	static {
		EMF$HELDITERATION = ReflectionHelper.getFieldFromClass(LivingEntityRenderer.class, "emf$heldIteration", EMFAnimationEntityContext.IterationContext.class, false);
	}

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearCachedEntityFromRenderer);
	}

	private void clearCachedEntityFromRenderer(RenderLivingEvent.Post<?, ?> event) {
		EMF$HELDITERATION.set(event.getRenderer(), null);
	}
}
