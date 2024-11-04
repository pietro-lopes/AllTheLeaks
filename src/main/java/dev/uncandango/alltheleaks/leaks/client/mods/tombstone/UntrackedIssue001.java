package dev.uncandango.alltheleaks.leaks.client.mods.tombstone;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.VarHandle;

@Issue(modId = "tombstone", versionRange = "[8.6.5,)", mixins = "main.LivingEntityRendererMixin",
description = "Clears `LivingEntityRenderer#entity` added via mixin on client level update")
public class UntrackedIssue001 {
	public static final VarHandle LIVING_ENTITY;

	static {
		LIVING_ENTITY = ReflectionHelper.getFieldFromClass(LivingEntityRenderer.class, "entity", LivingEntity.class, false);
	}

	public static void clearLivingEntityFromRenderer(LivingEntityRenderer<?,?> renderer){
		@Nullable var livingEntity = (LivingEntity) LIVING_ENTITY.get(renderer);
		if (livingEntity instanceof LocalPlayer player || livingEntity != null && livingEntity.level() != Minecraft.getInstance().level) {
			LIVING_ENTITY.set(renderer, (Object) null);
		}
	}
}
