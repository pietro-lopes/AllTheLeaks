package dev.uncandango.alltheleaks.leaks.common.mods.architectury;

import com.google.common.collect.Multimap;
import dev.architectury.networking.forge.NetworkManagerImpl;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.lang.invoke.VarHandle;

@Issue(modId = "architectury", versionRange = "[9.0.8,)")
public class UntrackedIssue001 {
	public static final VarHandle CLIENT_RECEIVABLES;

	static {
		CLIENT_RECEIVABLES = ReflectionHelper.getFieldFromClass(NetworkManagerImpl.class, "clientReceivables", Multimap.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::updateOnPlayerClone);
	}

	private void updateOnPlayerClone(PlayerEvent.Clone event) {
		var multiMap = (Multimap<Player, ResourceLocation>) CLIENT_RECEIVABLES.get();
		var originalId = event.getOriginal().getId();
		var newId = event.getEntity().getId();
		event.getEntity().setId(originalId);
		multiMap.putAll(event.getEntity(), multiMap.removeAll(event.getOriginal()));
		event.getEntity().setId(newId);
	}
}
