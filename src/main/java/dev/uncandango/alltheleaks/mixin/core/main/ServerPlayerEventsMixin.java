package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.player.ServerPlayerEvents;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static dev.uncandango.alltheleaks.leaks.common.mods.irons_spellbooks.UntrackedIssue001.updateLivingEntityOnSyncedSpellData;

@Mixin(value = ServerPlayerEvents.class, remap = false)
public class ServerPlayerEventsMixin {
	@WrapOperation(method = "onPlayerCloned", at = @At(value = "INVOKE", target = "Lio/redspace/ironsspellbooks/api/magic/MagicData;getSyncedData()Lio/redspace/ironsspellbooks/capabilities/magic/SyncedSpellData;", ordinal = 1))
	private static SyncedSpellData onPlayerCloned(MagicData instance, Operation<SyncedSpellData> original, @Local(argsOnly = true)PlayerEvent.Clone event) {
		try {
			return updateLivingEntityOnSyncedSpellData(instance, original, event);
		} catch (ExceptionInInitializerError e){
			return original.call(instance);
		}
	}
}
