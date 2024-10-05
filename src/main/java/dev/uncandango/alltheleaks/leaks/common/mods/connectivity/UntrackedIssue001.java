package dev.uncandango.alltheleaks.leaks.common.mods.connectivity;

import com.connectivity.networkstats.NetworkStatGatherer;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Issue(modId = "connectivity", versionRange = "[5.8,)")
public class UntrackedIssue001 {
	public static final VarHandle MINUTEDATA;
	public static final VarHandle RECORDINGDURATION;
	public static final VarHandle CONNECTIONPACKETDATA;

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearPacketData);
	}

	private void clearPacketData(ServerStoppedEvent event) {
		MINUTEDATA.set(new ArrayList<>(Arrays.asList(new ConcurrentHashMap[(int)RECORDINGDURATION.get()])));
		((Map<?,?>)CONNECTIONPACKETDATA.get()).clear();
	}

	static {
		MINUTEDATA = ReflectionHelper.getFieldFromClass(NetworkStatGatherer.class, "minuteData", List.class, true);
		RECORDINGDURATION = ReflectionHelper.getFieldFromClass(NetworkStatGatherer.class, "recordingDuration", int.class, true);
		CONNECTIONPACKETDATA = ReflectionHelper.getFieldFromClass(NetworkStatGatherer.class, "connectionPacketData", Map.class, true);
	}
}
