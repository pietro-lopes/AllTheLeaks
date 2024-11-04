package dev.uncandango.alltheleaks.leaks.client.mods.blue_skies;

import com.legacy.blue_skies.client.audio.ambient.DungeonAmbientSoundHandler;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.SkiesClientEventsAccessor;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppedEvent;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.VarHandle;

@Issue(modId = "blue_skies", versionRange = "[1.3.31,)", mixins = "accessor.SkiesClientEventsAccessor",
description = "Clears `ClientPacketListener#blue_skies$lastRidden` mixin field and updates `SkiesClientEvents#dungeonAmbientSoundHandler` on server stopped/client player clone")
public class UntrackedIssue001 {
	public static final VarHandle LAST_RIDDEN;

	static {
		LAST_RIDDEN = ReflectionHelper.getFieldFromClass(ClientPacketListener.class, "blue_skies$lastRidden", Entity.class, true);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::updatePlayer);
		gameBus.addListener(this::updatePlayerOnLogout);
	}

	private void updatePlayerOnLogout(ServerStoppedEvent event) {
		if (event.getServer() instanceof IntegratedServer) {
			var soundHandler = new DungeonAmbientSoundHandler(null, Minecraft.getInstance().getSoundManager());
			SkiesClientEventsAccessor.atl$setDungeonAmbientSoundHandler(soundHandler);
			@Nullable var lastRiddenEntity = (Entity)LAST_RIDDEN.get();
			if (lastRiddenEntity != null) {
				LAST_RIDDEN.set((Object) null);
			}
		}
	}

	private void updatePlayer(ClientPlayerNetworkEvent.Clone event) {
		var soundHandler = new DungeonAmbientSoundHandler(event.getNewPlayer(), Minecraft.getInstance().getSoundManager());
		SkiesClientEventsAccessor.atl$setDungeonAmbientSoundHandler(soundHandler);
		@Nullable var lastRiddenEntity = (Entity)LAST_RIDDEN.get();
		if (lastRiddenEntity != null) {
			if (lastRiddenEntity.isRemoved() || lastRiddenEntity.level() != event.getNewPlayer().level()) {
				LAST_RIDDEN.set((Object) null);
			}
		}
	}
}
