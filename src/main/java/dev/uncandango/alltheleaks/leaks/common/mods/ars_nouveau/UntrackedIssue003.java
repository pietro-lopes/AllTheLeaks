package dev.uncandango.alltheleaks.leaks.common.mods.ars_nouveau;

import com.hollingsworth.arsnouveau.api.ANFakePlayer;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.ConnectionAccessor;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.network.Connection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppedEvent;

import java.lang.invoke.VarHandle;

@Issue(modId = "ars_nouveau", versionRange = "*", mixins = "accessor.ConnectionAccessor")
public class UntrackedIssue003 {
	public static final VarHandle NETWORK_MANAGER;

	public UntrackedIssue003() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearFakePlayer);
	}

	static {
		NETWORK_MANAGER = ReflectionHelper.getFieldFromClass(ANFakePlayer.class, "NETWORK_MANAGER", Connection.class, true);
	}

	private void clearFakePlayer(ServerStoppedEvent event) {
		((ConnectionAccessor) (Connection)NETWORK_MANAGER.get()).setPacketListener(null);
	}
}
