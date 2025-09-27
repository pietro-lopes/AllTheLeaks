package dev.uncandango.alltheleaks.leaks.client.mods.entity_model_features;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import traben.entity_model_features.models.animation.EMFAnimationEntityContext;

import java.lang.invoke.VarHandle;

@Issue(modId = "entity_model_features", versionRange = "[3.0.0,)", description = "Clears `LivingEntityRenderer#emf$heldIteration` after rendering")
public class UntrackedIssue002 {
	public static final VarHandle EMF$HELDITERATION;

	public UntrackedIssue002() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearContextAfterRender);
	}

	static {
		EMF$HELDITERATION = ReflectionHelper.getFieldFromClass(LivingEntityRenderer.class, "emf$heldIteration", EMFAnimationEntityContext.IterationContext.class, false);
	}

	private void clearContextAfterRender(RenderLivingEvent.Post<?,?> event) {
		EMF$HELDITERATION.set(event.getRenderer(), (Object) null);
	}
}
