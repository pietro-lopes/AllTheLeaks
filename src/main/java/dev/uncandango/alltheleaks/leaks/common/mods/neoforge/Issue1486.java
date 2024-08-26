package dev.uncandango.alltheleaks.leaks.common.mods.neoforge;

import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.bus.EventBus;
import net.neoforged.bus.ListenerList;
import net.neoforged.bus.LockHelper;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "neoforge", versionRange = "[21.,)")
public class UntrackedIssue002 {
	public static final VarHandle LISTENER_LIST;
	public static final VarHandle REBUILD;
	public static final MethodHandle GET_READ_MAP;

	static {
		LISTENER_LIST = ReflectionHelper.getFieldFromClass(EventBus.class, "listenerLists", LockHelper.class, false);
		GET_READ_MAP = ReflectionHelper.getMethodFromClass(LockHelper.class, "getReadMap", MethodType.methodType(Map.class), false);
		REBUILD = ReflectionHelper.getFieldFromClass(ListenerList.class, "rebuild", boolean.class, false);
	}

	public UntrackedIssue002() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(EventPriority.LOWEST, this::rebuildListenersCache);
	}

	private void rebuildListenersCache(ServerStoppedEvent event) {
		var locker = LISTENER_LIST.get(NeoForge.EVENT_BUS);
		try {
			Map<Class<?>, ListenerList> map = (Map<Class<?>, ListenerList>) GET_READ_MAP.invoke(locker);
			for (var listener : map.values()) {
				boolean rebuild = (boolean) REBUILD.get(listener);
				if (rebuild) {
					listener.getListeners();
				}
			}
		} catch (Throwable e) {
			AllTheLeaks.LOGGER.error("Error while trying to rebuild listeners cache", e);
		}
	}
}
