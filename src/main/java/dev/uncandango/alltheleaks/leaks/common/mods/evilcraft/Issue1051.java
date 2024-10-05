package dev.uncandango.alltheleaks.leaks.common.mods.evilcraft;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import org.cyclops.evilcraft.enchantment.entityeffect.EnchantmentEntityEffectHealFromDamage;

import java.lang.invoke.VarHandle;

@Issue(modId = "evilcraft", issueId = "#1051", versionRange = "(,1.2.58]")
public class Issue1051 {
	private static final VarHandle LAST_DAMAGE_EVENT;

	public Issue1051() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearDamageEvent);
		gameBus.addListener(this::clearDamageEventOnClone);
	}

	private void clearDamageEvent(LevelEvent.Unload event){
		if (event.getLevel().isClientSide()) return;
		var postEvent = (LivingDamageEvent.Post) LAST_DAMAGE_EVENT.get();
		if (postEvent != null){
			if (postEvent.getEntity().isRemoved()) LAST_DAMAGE_EVENT.set((Object)null);
			if (postEvent.getSource().getEntity() != null) {
				if (postEvent.getSource().getEntity().isRemoved()) LAST_DAMAGE_EVENT.set((Object)null);
			}
		}
	}

	private void clearDamageEventOnClone(PlayerEvent.Clone event){
		if (event.getEntity().level().isClientSide()) return;
		var postEvent = (LivingDamageEvent.Post) LAST_DAMAGE_EVENT.get();
		if (postEvent != null){
			if (postEvent.getEntity().isRemoved()) LAST_DAMAGE_EVENT.set((Object)null);
			if (postEvent.getSource().getEntity() != null) {
				if (postEvent.getSource().getEntity().isRemoved()) LAST_DAMAGE_EVENT.set((Object)null);
			}
		}
	}

	static {
		LAST_DAMAGE_EVENT = ReflectionHelper.getFieldFromClass(EnchantmentEntityEffectHealFromDamage.class, "lastDamageEvent", LivingDamageEvent.Post.class, true);
	}
}
