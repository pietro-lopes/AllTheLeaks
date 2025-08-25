package dev.uncandango.alltheleaks.leaks.client.mods.entity_model_features;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.level.Level;
import traben.entity_model_features.models.animation.EMFAnimationEntityContext;
import traben.entity_model_features.utils.EMFEntity;
import traben.entity_texture_features.utils.ETFEntity;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;

@Issue(modId = "entity_model_features", versionRange = "[2.2.2,3.0.0)", mixins = "main.ATLLivingEntityRendererMixin", description = "Clears `LivingEntityRenderer#emf$heldIteration` when level changes")
public class UntrackedIssue001 {
	public static final VarHandle EMF$HELDITERATION;
	public static final MethodHandle EMF$IEMF_ENTITY;
	public static final MethodHandle ETF$GET_WORLD;

	static {
		EMF$HELDITERATION = ReflectionHelper.getFieldFromClass(LivingEntityRenderer.class, "emf$heldIteration", EMFAnimationEntityContext.IterationContext.class, false);
		EMF$IEMF_ENTITY = ReflectionHelper.getMethodFromClass(EMFAnimationEntityContext.IterationContext.class, "IEMFEntity", MethodType.methodType(EMFEntity.class), false);
		ETF$GET_WORLD = ReflectionHelper.getMethodFromClass(ETFEntity.class, "etf$getWorld", MethodType.methodType(Level.class), false);
		var enabled = ReflectionHelper.getFieldFromClass(LivingEntityRenderer.class, "atl$emfEnabled", boolean.class, true);
		enabled.set(true);
	}

	public static void clearCachedEntityFromRenderer(LivingEntityRenderer<?, ?> renderer, Level level) {

		var iteration = (EMFAnimationEntityContext.IterationContext) EMF$HELDITERATION.get(renderer);
		if (iteration == null) {
			return;
		}
		var iemf = iteration.IEMFEntity();
		if (iemf == null) {
			return;
		}
		var world = iemf.etf$getWorld();
		if (world == null) {
			return;
		}
		if (world != level) {
			EMF$HELDITERATION.set(renderer, null);
		}

	}

}
