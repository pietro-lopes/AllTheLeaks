package dev.uncandango.alltheleaks.leaks.client.mods.iceberg;

import com.anthonyhilyard.iceberg.renderer.CustomItemRenderer;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;

import java.lang.invoke.VarHandle;

@Issue(modId = "iceberg", versionRange = "[1.1.25,)", description = "Clears `CustomItemRenderer#blockEntity` on client level update")
public class UntrackedIssue003 {
	public static final VarHandle BLOCK_ENTITY;

	public UntrackedIssue003() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearBlockEntities);
	}

	static {
		BLOCK_ENTITY = ReflectionHelper.getFieldFromClass(CustomItemRenderer.class, "blockEntity", BlockEntity.class, true);
	}

	private void clearBlockEntities(UpdateableLevel.RenderEnginesUpdated event) {
		BLOCK_ENTITY.set((Object) null);
	}
}
