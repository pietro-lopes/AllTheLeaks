package dev.uncandango.alltheleaks.leaks.client.mods.iceberg;

import com.anthonyhilyard.iceberg.renderer.CustomItemRenderer;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraftforge.common.MinecraftForge;

import java.lang.invoke.VarHandle;

@Issue(modId = "iceberg", versionRange = "[1.1.10,)", description = "Clears `CustomItemRenderer#armorStand/horse/entity` on client level update")
public class UntrackedIssue002 {
	public static final VarHandle HORSE;
	public static final VarHandle ARMOR_STAND;
	public static final VarHandle ENTITY;

	static {
		HORSE = ReflectionHelper.getFieldFromClass(CustomItemRenderer.class, "horse", Horse.class, true);
		ARMOR_STAND = ReflectionHelper.getFieldFromClass(CustomItemRenderer.class, "armorStand", ArmorStand.class, true);
		ENTITY = ReflectionHelper.getFieldFromClass(CustomItemRenderer.class, "entity", Entity.class, true);
	}

	public UntrackedIssue002() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearEntities);
	}

	private void clearEntities(UpdateableLevel.RenderEnginesUpdated event) {
		HORSE.set((Object) null);
		ARMOR_STAND.set((Object) null);
		ENTITY.set((Object) null);
	}
}
