package dev.uncandango.alltheleaks.leaks.client.mods.twilightforest;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.common.Internal;
import net.minecraftforge.common.MinecraftForge;
import twilightforest.compat.jei.categories.TransformationPowderCategory;
import twilightforest.compat.jei.renderers.EntityRenderer;

import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "twilightforest", versionRange = "[4.3.2508,)", extraModDep = "jei", extraModDepVersions = "[15.8.2.24,)",
description = "Clears `EntityRenderer#ENTITY_MAP` on level update")
public class UntrackedIssue001 {
	public static final VarHandle ENTITY_RENDERER;
	public static final VarHandle ENTITY_MAP;

	static {
		ENTITY_RENDERER = ReflectionHelper.getFieldFromClass(TransformationPowderCategory.class, "entityRenderer", EntityRenderer.class, false);
		ENTITY_MAP = ReflectionHelper.getFieldFromClass(EntityRenderer.class, "ENTITY_MAP", Map.class, false);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearEntityMap);
	}

	@SuppressWarnings("rawtypes")
	private void clearEntityMap(UpdateableLevel.RenderEnginesUpdated event) {
		if (event.getLevel() != null) {
			IJeiRuntime jeiRuntime;
			try {
				jeiRuntime = Internal.getJeiRuntime();
			} catch (IllegalStateException ignored) {
				return;
			}
			var recipeCat = jeiRuntime.getRecipeManager().getRecipeCategory(TransformationPowderCategory.TRANSFORMATION);
			var entityRenderer = ENTITY_RENDERER.get(recipeCat);
			var entityMap = (Map) ENTITY_MAP.get(entityRenderer);
			entityMap.clear();
		}
	}
}
