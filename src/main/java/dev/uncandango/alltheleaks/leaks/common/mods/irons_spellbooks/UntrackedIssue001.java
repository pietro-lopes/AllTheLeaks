package dev.uncandango.alltheleaks.leaks.common.mods.irons_spellbooks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.lang.invoke.VarHandle;

@Issue(modId = "irons_spellbooks", versionRange = "[1.20.1-3.4.0,)", mixins = "main.ServerPlayerEventsMixin", description = "Update `SyncedSpellData#livingEntity` on player clone")
public class UntrackedIssue001 {
	public static final VarHandle LIVING_ENTITY;
	static {
		LIVING_ENTITY = ReflectionHelper.getFieldFromClass(SyncedSpellData.class, "livingEntity", LivingEntity.class, false);
	}

	public static SyncedSpellData updateLivingEntityOnSyncedSpellData(MagicData instance, Operation<SyncedSpellData> original, PlayerEvent.Clone event){
		var data = original.call(instance);
		LIVING_ENTITY.set(data, event.getEntity());
		return data;
	}
}
